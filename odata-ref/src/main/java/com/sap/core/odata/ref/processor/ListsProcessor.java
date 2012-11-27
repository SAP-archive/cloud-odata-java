package com.sap.core.odata.ref.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.enums.InlineCount;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.resultviews.GetComplexPropertyView;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;
import com.sap.core.odata.api.uri.resultviews.GetFunctionImportView;
import com.sap.core.odata.api.uri.resultviews.GetMediaResourceView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;

/**
 * Implementation of the centralized parts of OData processing,
 * allowing to use the simplified {@link ListsDataSource}
 * for the actual data handling
 * @author SAP AG
 */
public class ListsProcessor extends ODataSingleProcessor {

  private final ListsDataSource dataSource;

  public ListsProcessor(ListsDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ODataResponse readServiceDocument(final GetServiceDocumentView uriParserResultView) throws ODataException {
    return ODataResponse
        .status(HttpStatusCodes.OK)
        .entity("this should be the service document")
        .build();
  }

  @Override
  public ODataResponse readEntitySet(final GetEntitySetView uriParserResultView) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments()));

    applySystemQueryOptions(
        uriParserResultView.getTargetEntitySet(),
        data,
        uriParserResultView.getInlineCount(),
        uriParserResultView.getFilter(),
        uriParserResultView.getOrderBy(),
        uriParserResultView.getSkipToken(),
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .entity(data.toString())
        .build();
  }

  @Override
  public ODataResponse countEntitySet(final GetEntitySetCountView uriParserResultView) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments()));

    applySystemQueryOptions(
        uriParserResultView.getTargetEntitySet(),
        data,
        null,
        uriParserResultView.getFilter(),
        null,
        null,
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    return ODataResponse
        .status(HttpStatusCodes.OK)
        //.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
        .entity(String.valueOf(data.size()))
        .build();
  }

  @Override
  public ODataResponse readEntityLinks(final GetEntitySetLinksView uriParserResultView) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments()));

    applySystemQueryOptions(
        uriParserResultView.getTargetEntitySet(),
        data,
        uriParserResultView.getInlineCount(),
        uriParserResultView.getFilter(),
        // uriParserResultView.getOrderBy(),
        null,
        uriParserResultView.getSkipToken(),
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .entity("Links to " + data)
        .build();
  }

  @Override
  public ODataResponse countEntityLinks(final GetEntitySetLinksCountView uriParserResultView) throws ODataException {
    return countEntitySet((GetEntitySetCountView) uriParserResultView);
  }

  @Override
  public ODataResponse readEntity(final GetEntityView uriParserResultView) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    if (appliesFilter(data, uriParserResultView.getFilter()))
      return ODataResponse
          .status(HttpStatusCodes.OK)
          .entity(data.toString())
          .build();
    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public ODataResponse existsEntity(final GetEntityCountView uriParserResultView) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    return ODataResponse
        .status(HttpStatusCodes.OK)
        //.header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
        .entity(appliesFilter(data, uriParserResultView.getFilter()) ? "1" : "0")
        .build();
  }

  @Override
  public ODataResponse readEntityLink(final GetEntityLinkView uriParserResultView) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    // if (appliesFilter(data, uriParserResultView.getFilter()))
    if (data != null)
      return ODataResponse
          .status(HttpStatusCodes.OK)
          .entity("Link to " + data)
          .build();
    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public ODataResponse existsEntityLink(final GetEntityLinkCountView uriParserResultView) throws ODataException {
    return existsEntity((GetEntityCountView) uriParserResultView);
  }

  @Override
  public ODataResponse readEntityComplexProperty(final GetComplexPropertyView uriParserResultView) throws ODataException {
    Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    // if (!appliesFilter(data, uriParserResultView.getFilter()))
    //   throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    for (EdmProperty property : uriParserResultView.getPropertyPath())
      data = getPropertyValue(data, property);

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .entity(data.toString())
        .build();
  }

  @Override
  public ODataResponse readEntitySimpleProperty(final GetSimplePropertyView uriParserResultView) throws ODataException {
    return readEntityComplexProperty((GetComplexPropertyView) uriParserResultView);
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue(final GetSimplePropertyView uriParserResultView) throws ODataException {
    Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    // if (!appliesFilter(data, uriParserResultView.getFilter()))
    //   throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    EdmProperty property = null;
    for (EdmProperty intermediateProperty : uriParserResultView.getPropertyPath()){
      data = getPropertyValue(data, property = intermediateProperty);
    }
    
    return ODataResponse
        .status(HttpStatusCodes.OK)
        //.header(HttpHeaders.CONTENT_TYPE,
        //            (EdmSimpleType) property.getType() == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance() ?
        //                property.getMimeType() : MediaType.TEXT_PLAIN)
        .entity(data.toString())
        .build();
  }

  @Override
  public ODataResponse readEntityMedia(final GetMediaResourceView uriParserResultView) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    if (!appliesFilter(data, uriParserResultView.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    StringBuilder mimeType = new StringBuilder();
    final byte[] binaryData = dataSource.readBinaryData(uriParserResultView.getTargetEntitySet(), data, mimeType);

    return ODataResponse
        .status(HttpStatusCodes.OK)
//        .header(HttpHeaders.CONTENT_TYPE,
//            mimeType.toString().isEmpty() ? MediaType.APPLICATION_OCTET_STREAM : mimeType.toString())
        .entity(binaryData)
        .build();
  }

  @Override
  public ODataResponse executeFunctionImport(final GetFunctionImportView uriParserResultView) throws ODataException {
    final Object data = dataSource.readData(
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        null);

    return ODataResponse
        .status(HttpStatusCodes.OK)
        .entity(data.toString())
        .build();
  }

  @Override
  public ODataResponse executeFunctionImportValue(final GetFunctionImportView uriParserResultView) throws ODataException {
    final EdmFunctionImport functionImport = uriParserResultView.getFunctionImport();
    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        null);

    return ODataResponse
        .status(HttpStatusCodes.OK)
        //        .header(HttpHeaders.CONTENT_TYPE,
        //            (EdmSimpleType) functionImport.getReturnType().getType() == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance() ?
        //                MediaType.APPLICATION_OCTET_STREAM : MediaType.TEXT_PLAIN)
        .entity(data.toString())
        .build();
  }

  private HashMap<String, Object> mapKey(final List<KeyPredicate> keys) throws EdmException {
    HashMap<String, Object> keyMap = new HashMap<String, Object>();
    for (final KeyPredicate key : keys) {
      final EdmProperty property = key.getProperty();
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      keyMap.put(property.getName(), type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets()));
    }
    return keyMap;
  }

  private Map<String, Object> mapFunctionParameters(final Map<String, EdmLiteral> functionImportParameters) throws EdmSimpleTypeException {
    if (functionImportParameters == null) {
      return Collections.emptyMap();
    } else {
      HashMap<String, Object> parameterMap = new HashMap<String, Object>();
      for (final String parameterName : functionImportParameters.keySet()) {
        final EdmLiteral literal = functionImportParameters.get(parameterName);
        final EdmSimpleType type = (EdmSimpleType) literal.getType();
        parameterMap.put(parameterName, type.valueOfString(literal.getLiteral(), EdmLiteralKind.DEFAULT, null));
      }
      return parameterMap;
    }
  }

  private Object retrieveData(final EdmEntitySet startEntitySet, final List<KeyPredicate> keyPredicates, final EdmFunctionImport functionImport, final Map<String, Object> functionImportParameters, final List<NavigationSegment> navigationSegments) throws ODataException {
    Object data;
    final HashMap<String, Object> keys = mapKey(keyPredicates);

    if (functionImport == null)
      if (keys.isEmpty())
        data = dataSource.readData(startEntitySet);
      else
        data = dataSource.readData(startEntitySet, keys);
    else
      data = dataSource.readData(functionImport, functionImportParameters, keys);

    EdmEntitySet currentEntitySet =
        functionImport == null ? startEntitySet : functionImport.getEntitySet();
    for (NavigationSegment navigationSegment : navigationSegments) {
      data = dataSource.readRelatedData(
          currentEntitySet,
          data,
          navigationSegment.getEntitySet(),
          mapKey(navigationSegment.getKeyPredicates()));
      currentEntitySet = navigationSegment.getEntitySet();
    }

    return data;
  }

  private <T> Integer applySystemQueryOptions(final EdmEntitySet targetEntitySet, List<T> data, final InlineCount inlineCount, final String filter, final String orderBy, final String skipToken, final int skip, final Integer top) throws ODataException {
    if (filter != null)
      for (T element : data)
        if (!appliesFilter(element, filter))
          data.remove(element);

    final Integer count = inlineCount == InlineCount.ALLPAGES ? data.size() : null;

    if (orderBy != null)
      throw new ODataNotImplementedException();
    else if (skipToken != null || skip != 0 || top != null)
      Collections.sort(data, new Comparator<T>() {
        @Override
        public int compare(T entity1, T entity2) {
          try {
            return getSkipToken(entity1, targetEntitySet).compareTo(getSkipToken(entity2, targetEntitySet));
          } catch (ODataException e) {
            return 0;
          }
        }
      });

    if (skipToken != null)
      while (!getSkipToken(data.get(0), targetEntitySet).equals(skipToken))
        data.remove(0);

    for (int i = 0; i < skip; i++)
      data.remove(0);

    if (top != null)
      while (data.size() > top)
        data.remove(top.intValue());

    return count;
  }

  private <T> boolean appliesFilter(final T data, final String filter) throws ODataException {
    if (data == null)
      return false;
    if (filter == null)
      return true;
    // TODO: implement filter evaluation
    throw new ODataNotImplementedException();
  }

  private <T> String getSkipToken(final T data, final EdmEntitySet entitySet) throws ODataException {
    String skipToken = "";
    for (EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      skipToken = skipToken.concat(type.valueToString(getPropertyValue(data, property), EdmLiteralKind.DEFAULT, property.getFacets()));
    }
    return skipToken;
  }

  private <T> Object getPropertyValue(final T data, final EdmProperty property) throws ODataException {
    final String prefix = property.getType().getKind() == EdmTypeKind.SIMPLE && property.getType() == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance() ? "is" : "get";
    final String methodName = property.getMapping() == null ?
        prefix + property.getName() : property.getMapping().getValue();
    try {
      return data.getClass().getMethod(methodName).invoke(data);
    } catch (SecurityException e) {
      throw new ODataNotFoundException(null);
    } catch (NoSuchMethodException e) {
      throw new ODataNotFoundException(null);
    } catch (IllegalArgumentException e) {
      throw new ODataNotFoundException(null);
    } catch (IllegalAccessException e) {
      throw new ODataNotFoundException(null);
    } catch (InvocationTargetException e) {
      throw new ODataNotFoundException(null);
    }
  }
}
