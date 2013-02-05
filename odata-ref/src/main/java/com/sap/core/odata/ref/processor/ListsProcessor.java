package com.sap.core.odata.ref.processor;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.LiteralExpression;
import com.sap.core.odata.api.uri.expression.MemberExpression;
import com.sap.core.odata.api.uri.expression.MethodExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.OrderExpression;
import com.sap.core.odata.api.uri.expression.PropertyExpression;
import com.sap.core.odata.api.uri.expression.SortOrder;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetComplexPropertyUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;
import com.sap.core.odata.api.uri.info.GetMediaResourceUriInfo;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.ref.processor.ListsDataSource.BinaryData;

/**
 * Implementation of the centralized parts of OData processing,
 * allowing to use the simplified {@link ListsDataSource}
 * for the actual data handling
 * @author SAP AG
 */
public class ListsProcessor extends ODataSingleProcessor {

  private static final int SERVER_PAGING_SIZE = 100;

  private final ListsDataSource dataSource;

  public ListsProcessor(ListsDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments()));

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final InlineCount inlineCountType = uriInfo.getInlineCount();
    final Integer count = applySystemQueryOptions(
        entitySet,
        data,
        uriInfo.getFilter(),
        inlineCountType,
        uriInfo.getOrderBy(),
        uriInfo.getSkipToken(),
        uriInfo.getSkip(),
        uriInfo.getTop());

    // Limit the number of returned entities and provide a "next" link
    // if there are further entities.
    // Almost all system query options in the current request must be carried
    // over to the URI for the "next" link, with the exception of $skiptoken
    // and $skip; this is missing currently.
    String nextSkipToken = null;
    if (data.size() > SERVER_PAGING_SIZE
        && uriInfo.getFilter() == null
        && inlineCountType == null
        && uriInfo.getOrderBy() == null
        && uriInfo.getTop() == null
        && uriInfo.getExpand().isEmpty()
        && uriInfo.getSelect().isEmpty()) {
      if (uriInfo.getOrderBy() == null
          && uriInfo.getSkipToken() == null
          && uriInfo.getSkip() == null
          && uriInfo.getTop() == null) // applySystemQueryOptions did not sort
        sortInDefaultOrder(entitySet, data);
      nextSkipToken = getSkipToken(entitySet, data.get(SERVER_PAGING_SIZE));
      while (data.size() > SERVER_PAGING_SIZE)
        data.remove(SERVER_PAGING_SIZE);
    }

    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
    for (final Object entryData : data)
      values.add(getStructuralTypeValueMap(entryData, entitySet.getEntityType()));

    ODataContext context = getContext();
    final EntityProviderProperties feedProperties = EntityProviderProperties
        .serviceRoot(context.getPathInfo().getServiceRoot())
        .inlineCountType(inlineCountType)
        .inlineCount(count)
        .skipToken(nextSkipToken)
        .build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeFeed");

    final ODataResponse response = EntityProvider.writeFeed(contentType, entitySet, values, feedProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).status(HttpStatusCodes.OK).build();
  }

  @Override
  public ODataResponse countEntitySet(final GetEntitySetCountUriInfo uriInfo, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments()));

    applySystemQueryOptions(
        uriInfo.getTargetEntitySet(),
        data,
        uriInfo.getFilter(),
        null,
        null,
        null,
        uriInfo.getSkip(),
        uriInfo.getTop());

    return ODataResponse.fromResponse(EntityProvider.writeText(String.valueOf(data.size())))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse readEntityLinks(final GetEntitySetLinksUriInfo uriInfo, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments()));

    final Integer count = applySystemQueryOptions(
        uriInfo.getTargetEntitySet(),
        data,
        uriInfo.getFilter(),
        uriInfo.getInlineCount(),
        null, // uriInfo.getOrderBy(),
        uriInfo.getSkipToken(),
        uriInfo.getSkip(),
        uriInfo.getTop());

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();

    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
    for (final Object entryData : data) {
      Map<String, Object> entryValues = new HashMap<String, Object>();
      for (final EdmProperty property : entitySet.getEntityType().getKeyProperties())
        entryValues.put(property.getName(), getPropertyValue(entryData, property));
      values.add(entryValues);
    }

    ODataContext context = getContext();
    final EntityProviderProperties entryProperties = EntityProviderProperties
        .serviceRoot(context.getPathInfo().getServiceRoot()).inlineCount(count).build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeLinks");

    final ODataResponse response = EntityProvider.writeLinks(contentType, entitySet, values, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).status(HttpStatusCodes.OK).build();
  }

  @Override
  public ODataResponse countEntityLinks(final GetEntitySetLinksCountUriInfo uriInfo, final String contentType) throws ODataException {
    return countEntitySet((GetEntitySetCountUriInfo) uriInfo, contentType);
  }

  @Override
  public ODataResponse readEntity(final GetEntityUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final Map<String, Object> values = getStructuralTypeValueMap(data, entitySet.getEntityType());
    final ODataResponse response = writeEntry(entitySet, values, contentType);

    return ODataResponse.fromResponse(response).status(HttpStatusCodes.OK).build();
  }

  @Override
  public ODataResponse existsEntity(final GetEntityCountUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    return ODataResponse.fromResponse(EntityProvider.writeText(
        appliesFilter(data, uriInfo.getFilter()) ? "1" : "0"))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse deleteEntity(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    dataSource.deleteData(
        uriInfo.getStartEntitySet(),
        mapKey(uriInfo.getKeyPredicates()));
    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse createEntity(final PostUriInfo uriInfo, InputStream content, final String requestContentType, final String contentType) throws ODataException {
    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final EdmEntityType entityType = entitySet.getEntityType();

    if (!uriInfo.getNavigationSegments().isEmpty())
      throw new ODataNotImplementedException();

    Object data = dataSource.newDataObject(entitySet);

    if (entityType.hasStream()) {
      dataSource.createData(entitySet, data);
      dataSource.writeBinaryData(entitySet, data,
          new BinaryData(EntityProvider.readBinary(content), requestContentType));

    } else {
      final ODataEntry entryValues = parseEntry(entitySet, content, requestContentType);

      setStructuralTypeValuesFromMap(data, entityType, entryValues.getProperties(), true);

      dataSource.createData(entitySet, data);

      linkEntity(entitySet, data, entryValues.getMetadata().getAssociationUris());
    }

    Map<String, Object> values = getStructuralTypeValueMap(data, entityType);
    final ODataResponse response = writeEntry(entitySet, values, contentType);

    // TODO: set correct Location header
    final ODataContext context = getContext();
    final String location = context.getPathInfo().getServiceRoot() + "";

    return ODataResponse.fromResponse(response)
        .status(HttpStatusCodes.CREATED)
        .header(HttpHeaders.LOCATION, location)
        .build();
  }

  @Override
  public ODataResponse updateEntity(final PutMergePatchUriInfo uriInfo, InputStream content, final String requestContentType, final boolean merge, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final ODataEntry entryValues = parseEntry(entitySet, content, requestContentType);

    final EdmEntityType entityType = entitySet.getEntityType();
    setStructuralTypeValuesFromMap(data, entityType, entryValues.getProperties(), merge);

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse readEntityLink(final GetEntityLinkUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    // if (!appliesFilter(data, uriInfo.getFilter()))
    if (data == null)
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();

    Map<String, Object> values = new HashMap<String, Object>();
    for (final EdmProperty property : entitySet.getEntityType().getKeyProperties())
      values.put(property.getName(), getPropertyValue(data, property));

    ODataContext context = getContext();
    final EntityProviderProperties entryProperties = EntityProviderProperties
        .serviceRoot(context.getPathInfo().getServiceRoot()).build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeLink");

    final ODataResponse response = EntityProvider.writeLink(contentType, entitySet, values, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).status(HttpStatusCodes.OK).build();
  }

  @Override
  public ODataResponse existsEntityLink(final GetEntityLinkCountUriInfo uriInfo, final String contentType) throws ODataException {
    return existsEntity((GetEntityCountUriInfo) uriInfo, contentType);
  }

  @Override
  public ODataResponse deleteEntityLink(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    final List<NavigationSegment> navigationSegments = uriInfo.getNavigationSegments();
    final List<NavigationSegment> previousSegments = navigationSegments.subList(0, navigationSegments.size() - 1);

    final Object sourceData = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        previousSegments);

    EdmEntitySet entitySet;
    if (previousSegments.isEmpty())
      entitySet = uriInfo.getStartEntitySet();
    else
      entitySet = previousSegments.get(previousSegments.size() - 1).getEntitySet();
    final NavigationSegment navigationSegment = navigationSegments.get(navigationSegments.size() - 1);
    final Map<String, Object> keys = mapKey(navigationSegment.getKeyPredicates());
    final Object targetData = dataSource.readRelatedData(
        entitySet, sourceData, navigationSegment.getEntitySet(), keys);

    // if (!appliesFilter(targetData, uriInfo.getFilter()))
    if (targetData == null)
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    dataSource.deleteRelation(entitySet, sourceData, navigationSegment.getEntitySet(), keys);
    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse createEntityLink(final PostUriInfo uriInfo, InputStream content, final String requestContentType, final String contentType) throws ODataException {
    final List<NavigationSegment> navigationSegments = uriInfo.getNavigationSegments();
    final List<NavigationSegment> previousSegments = navigationSegments.subList(0, navigationSegments.size() - 1);

    final Object sourceData = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        previousSegments);

    EdmEntitySet entitySet;
    if (previousSegments.isEmpty())
      entitySet = uriInfo.getStartEntitySet();
    else
      entitySet = previousSegments.get(previousSegments.size() - 1).getEntitySet();
    final EdmEntitySet targetEntitySet = uriInfo.getTargetEntitySet();

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "readLink");

    final String uriString = EntityProvider.readLink(requestContentType, targetEntitySet, content);

    context.stopRuntimeMeasurement(timingHandle);

    final Map<String, Object> targetKeys = parseLink(targetEntitySet, uriString);
    if (targetKeys == null)
      throw new ODataBadRequestException(ODataBadRequestException.BODY);

    dataSource.writeRelation(entitySet, sourceData, targetEntitySet, targetKeys);

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse updateEntityLink(final PutMergePatchUriInfo uriInfo, InputStream content, final String requestContentType, final String contentType) throws ODataException {
    throw new ODataNotImplementedException();
  }

  @Override
  public ODataResponse readEntityComplexProperty(final GetComplexPropertyUriInfo uriInfo, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    // if (!appliesFilter(data, uriInfo.getFilter()))
    //   throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    data = getPropertyValue(data, propertyPath);

    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    final Object value = property.isSimple() ?
        data : getStructuralTypeValueMap(data, (EdmStructuralType) property.getType());

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeProperty");

    final ODataResponse response = EntityProvider.writeProperty(contentType, property, value);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).status(HttpStatusCodes.OK).build();
  }

  @Override
  public ODataResponse readEntitySimpleProperty(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
    return readEntityComplexProperty((GetComplexPropertyUriInfo) uriInfo, contentType);
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue(final GetSimplePropertyUriInfo uriInfo, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    // if (!appliesFilter(data, uriInfo.getFilter()))
    //   throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    Object value = getPropertyValue(data, propertyPath);

    if (property.getMapping() != null && property.getMapping().getMimeType() != null) {
      Map<String, Object> valueWithMimeType = new HashMap<String, Object>();
      valueWithMimeType.put(property.getName(), value);
      final String mimeTypeMappingName = property.getMapping().getMimeType();
      valueWithMimeType.put(mimeTypeMappingName, getValue(data, mimeTypeMappingName));
      value = valueWithMimeType;
    }

    return ODataResponse.fromResponse(EntityProvider.writePropertyValue(property, value))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse deleteEntitySimplePropertyValue(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (data == null)
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    data = getPropertyValue(data, propertyPath.subList(0, propertyPath.size() - 1));
    setPropertyValue(data, property, null);
    if (property.getMapping() != null && property.getMapping().getMimeType() != null)
      setValue(data, getSetterMethodName(property.getMapping().getMimeType()), null);

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse updateEntityComplexProperty(final PutMergePatchUriInfo uriInfo, InputStream content, final String requestContentType, final boolean merge, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    data = getPropertyValue(data, propertyPath.subList(0, propertyPath.size() - 1));

    ODataContext context = getContext();
    int timingHandle = context.startRuntimeMeasurement("EntityConsumer", "readProperty");

    Map<String, Object> values;
    try {
      values = EntityProvider.readProperty(requestContentType, property, content);
    } catch (EntityProviderException e) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
    }

    context.stopRuntimeMeasurement(timingHandle);

    final Object value = values.get(property.getName());
    if (property.isSimple()) {
      setPropertyValue(data, property, value);
    } else {
      @SuppressWarnings("unchecked")
      final Map<String, Object> propertyValue = (Map<String, Object>) value;
      setStructuralTypeValuesFromMap(getPropertyValue(data, property), (EdmStructuralType) property.getType(), propertyValue, merge);
    }

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse updateEntitySimpleProperty(final PutMergePatchUriInfo uriInfo, InputStream content, final String requestContentType, final String contentType) throws ODataException {
    return updateEntityComplexProperty(uriInfo, content, requestContentType, false, contentType);
  }

  @Override
  public ODataResponse updateEntitySimplePropertyValue(final PutMergePatchUriInfo uriInfo, InputStream content, final String requestContentType, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    data = getPropertyValue(data, propertyPath.subList(0, propertyPath.size() - 1));

    ODataContext context = getContext();
    int timingHandle = context.startRuntimeMeasurement("EntityConsumer", "readPropertyValue");

    Object value;
    try {
      value = EntityProvider.readPropertyValue(property, content);
    } catch (EntityProviderException e) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
    }

    context.stopRuntimeMeasurement(timingHandle);

    setPropertyValue(data, property, value);
    if (property.getMapping() != null && property.getMapping().getMimeType() != null)
      setValue(data, getSetterMethodName(property.getMapping().getMimeType()), requestContentType);

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse readEntityMedia(final GetMediaResourceUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final BinaryData binaryData = dataSource.readBinaryData(entitySet, data);
    final String mimeType = binaryData.getMimeType() == null ?
        HttpContentType.APPLICATION_OCTET_STREAM : binaryData.getMimeType();

    return ODataResponse.fromResponse(EntityProvider.writeBinary(mimeType, binaryData.getData()))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse deleteEntityMedia(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (data == null)
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    dataSource.writeBinaryData(uriInfo.getTargetEntitySet(), data, new BinaryData(null, null));

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse updateEntityMedia(final PutMergePatchUriInfo uriInfo, InputStream content, final String requestContentType, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "readBinary");

    final byte[] value = EntityProvider.readBinary(content);

    context.stopRuntimeMeasurement(timingHandle);

    dataSource.writeBinaryData(uriInfo.getTargetEntitySet(), data, new BinaryData(value, requestContentType));

    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse executeFunctionImport(final GetFunctionImportUriInfo uriInfo, final String contentType) throws ODataException {
    final EdmFunctionImport functionImport = uriInfo.getFunctionImport();
    final EdmType type = functionImport.getReturnType().getType();

    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        null);

    Object value;
    if (type.getKind() == EdmTypeKind.SIMPLE)
      value = data;
    else if (functionImport.getReturnType().getMultiplicity() == EdmMultiplicity.MANY) {
      List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
      for (final Object typeData : (List<?>) data)
        values.add(getStructuralTypeValueMap(typeData, (EdmStructuralType) type));
      value = values;
    } else {
      value = getStructuralTypeValueMap(data, (EdmStructuralType) type);
    }

    ODataContext context = getContext();
    final EntityProviderProperties entryProperties = EntityProviderProperties
        .serviceRoot(context.getPathInfo().getServiceRoot()).build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeFunctionImport");

    final ODataResponse response = EntityProvider.writeFunctionImport(contentType, functionImport, value, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).status(HttpStatusCodes.OK).build();
  }

  @Override
  public ODataResponse executeFunctionImportValue(final GetFunctionImportUriInfo uriInfo, final String contentType) throws ODataException {
    final EdmFunctionImport functionImport = uriInfo.getFunctionImport();
    final EdmSimpleType type = (EdmSimpleType) functionImport.getReturnType().getType();

    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        null);

    ODataResponse response;
    if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
      response = EntityProvider.writeBinary(HttpContentType.APPLICATION_OCTET_STREAM, (byte[]) data);
    } else {
      final String value = type.valueToString(data, EdmLiteralKind.DEFAULT, null);
      response = EntityProvider.writeText(value == null ? "" : value);
    }
    return ODataResponse.fromResponse(response)
        .status(HttpStatusCodes.OK)
        .build();
  }

  private static Map<String, Object> mapKey(final List<KeyPredicate> keys) throws EdmException {
    HashMap<String, Object> keyMap = new HashMap<String, Object>();
    for (final KeyPredicate key : keys) {
      final EdmProperty property = key.getProperty();
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      keyMap.put(property.getName(), type.valueOfString(key.getLiteral(), EdmLiteralKind.DEFAULT, property.getFacets(), type.getDefaultType()));
    }
    return keyMap;
  }

  private static Map<String, Object> mapFunctionParameters(final Map<String, EdmLiteral> functionImportParameters) throws EdmSimpleTypeException {
    if (functionImportParameters == null) {
      return Collections.emptyMap();
    } else {
      HashMap<String, Object> parameterMap = new HashMap<String, Object>();
      for (final String parameterName : functionImportParameters.keySet()) {
        final EdmLiteral literal = functionImportParameters.get(parameterName);
        final EdmSimpleType type = literal.getType();
        parameterMap.put(parameterName, type.valueOfString(literal.getLiteral(), EdmLiteralKind.DEFAULT, null, type.getDefaultType()));
      }
      return parameterMap;
    }
  }

  private Object retrieveData(final EdmEntitySet startEntitySet, final List<KeyPredicate> keyPredicates, final EdmFunctionImport functionImport, final Map<String, Object> functionImportParameters, final List<NavigationSegment> navigationSegments) throws ODataException {
    Object data;
    final Map<String, Object> keys = mapKey(keyPredicates);

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "retrieveData");

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

    context.stopRuntimeMeasurement(timingHandle);

    return data;
  }

  private ODataResponse writeEntry(final EdmEntitySet entitySet, final Map<String, Object> values, final String contentType) throws ODataException, EntityProviderException {
    ODataContext context = getContext();
    final EntityProviderProperties entryProperties = EntityProviderProperties
        .serviceRoot(context.getPathInfo().getServiceRoot()).build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeEntry");

    final ODataResponse response = EntityProvider.writeEntry(contentType, entitySet, values, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return response;
  }

  private ODataEntry parseEntry(final EdmEntitySet entitySet, InputStream content, final String requestContentType) throws ODataBadRequestException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityConsumer", "readEntry");

    ODataEntry entryValues;
    try {
      entryValues = EntityProvider.readEntry(requestContentType, entitySet, content);
    } catch (EntityProviderException e) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
    }

    context.stopRuntimeMeasurement(timingHandle);

    return entryValues;
  }

  private Map<String, Object> parseLink(final EdmEntitySet targetEntitySet, final String uriString) throws ODataException {
    final String serviceRoot = getContext().getPathInfo().getServiceRoot().toString();
    final String path = uriString.startsWith(serviceRoot.toString()) ?
        uriString.substring(serviceRoot.length()) : uriString;
    final PathSegment pathSegment = new PathSegment() {
      @Override
      public String getPath() {
        return path;
      }
      @Override
      public Map<String, List<String>> getMatrixParameters() {
        return null;
      }
    };

    final Edm edm = getContext().getService().getEntityDataModel();
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("UriParser", "parse");

    UriInfo uri = null;
    try {
      uri = UriParser.parse(edm, Arrays.asList(pathSegment), Collections.<String, String> emptyMap());
    } catch (ODataException e) {
      // We don't understand the link target.  This could also be seen as an error.
    }

    context.stopRuntimeMeasurement(timingHandle);

    if (uri == null)
      return null;
    else if (uri.getTargetEntitySet() == null
        || uri.getTargetEntitySet() != targetEntitySet
        || !uri.getNavigationSegments().isEmpty()
        || uri.getKeyPredicates().isEmpty())
      throw new ODataBadRequestException(ODataBadRequestException.BODY);
    else
      return mapKey(uri.getKeyPredicates());
  }

  private void linkEntity(final EdmEntitySet entitySet, Object data, final Map<String, String> links) throws ODataException {
    final EdmEntityType entityType = entitySet.getEntityType();
    for (final String navigationPropertyName : links.keySet()) {
      final String uriString = links.get(navigationPropertyName);
      final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) entityType.getProperty(navigationPropertyName);
      final EdmEntitySet targetEntitySet = entitySet.getRelatedEntitySet(navigationProperty);
      final Map<String, Object> key = parseLink(targetEntitySet, uriString);
      if (key != null)
        dataSource.writeRelation(entitySet, data, targetEntitySet, key);
    }
  }

  private <T> Integer applySystemQueryOptions(final EdmEntitySet entitySet, List<T> data, final FilterExpression filter, final InlineCount inlineCount, final OrderByExpression orderBy, final String skipToken, final Integer skip, final Integer top) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "applySystemQueryOptions");

    if (filter != null)
      // Remove all elements the filter does not apply for.
      // A for-each loop would not work with "remove", see Java documentation.
      for (Iterator<T> iterator = data.iterator(); iterator.hasNext();)
        if (!appliesFilter(iterator.next(), filter))
          iterator.remove();

    final Integer count = inlineCount == InlineCount.ALLPAGES ? data.size() : null;

    if (orderBy != null)
      sort(data, orderBy);
    else if (skipToken != null || skip != null || top != null)
      sortInDefaultOrder(entitySet, data);

    if (skipToken != null)
      while (!data.isEmpty() && !getSkipToken(entitySet, data.get(0)).equals(skipToken))
        data.remove(0);

    if (skip != null)
      if (skip >= data.size())
        data.clear();
      else
        for (int i = 0; i < skip; i++)
          data.remove(0);

    if (top != null)
      while (data.size() > top)
        data.remove(top.intValue());

    context.stopRuntimeMeasurement(timingHandle);

    return count;
  }

  private static <T> void sort(List<T> data, final OrderByExpression orderBy) {
    Collections.sort(data, new Comparator<T>() {
      @Override
      public int compare(final T entity1, final T entity2) {
        try {
          int result = 0;
          for (final OrderExpression expression : orderBy.getOrders()) {
            result = evaluateExpression(entity1, expression.getExpression()).compareTo(
                evaluateExpression(entity2, expression.getExpression()));
            if (expression.getSortOrder() == SortOrder.desc)
              result = -result;
            if (result != 0)
              break;
          }
          return result;
        } catch (ODataException e) {
          return 0;
        }
      }
    });
  }

  private static <T> void sortInDefaultOrder(final EdmEntitySet entitySet, List<T> data) {
    Collections.sort(data, new Comparator<T>() {
      @Override
      public int compare(final T entity1, final T entity2) {
        try {
          return getSkipToken(entitySet, entity1).compareTo(getSkipToken(entitySet, entity2));
        } catch (ODataException e) {
          return 0;
        }
      }
    });
  }

  private <T> boolean appliesFilter(final T data, final FilterExpression filter) throws ODataException {
    if (data == null)
      return false;
    if (filter == null)
      return true;

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "appliesFilter");

    try {
      return evaluateExpression(data, filter.getExpression()).equals("true");
    } catch (RuntimeException e) {
      return false;
    } finally {
      context.stopRuntimeMeasurement(timingHandle);
    }
  }

  private static <T> String evaluateExpression(final T data, final CommonExpression expression) throws ODataException {
    switch (expression.getKind()) {
    case UNARY:
      final UnaryExpression unaryExpression = (UnaryExpression) expression;
      final String operand = evaluateExpression(data, unaryExpression.getOperand());

      switch (unaryExpression.getOperator()) {
      case NOT:
        return Boolean.toString(!Boolean.parseBoolean(operand));
      case MINUS:
        if (operand.startsWith("-"))
          return operand.substring(1);
        else
          return "-" + operand;
      default:
        throw new ODataNotImplementedException();
      }

    case BINARY:
      final BinaryExpression binaryExpression = (BinaryExpression) expression;
      final EdmSimpleType type = (EdmSimpleType) binaryExpression.getLeftOperand().getEdmType();
      final String left = evaluateExpression(data, binaryExpression.getLeftOperand());
      final String right = evaluateExpression(data, binaryExpression.getRightOperand());

      switch (binaryExpression.getOperator()) {
      case ADD:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance())
          return Double.toString(Double.valueOf(left) + Double.valueOf(right));
        else
          return Long.toString(Long.valueOf(left) + Long.valueOf(right));
      case SUB:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance())
          return Double.toString(Double.valueOf(left) - Double.valueOf(right));
        else
          return Long.toString(Long.valueOf(left) - Long.valueOf(right));
      case MUL:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance())
          return Double.toString(Double.valueOf(left) * Double.valueOf(right));
        else
          return Long.toString(Long.valueOf(left) * Long.valueOf(right));
      case DIV:
        String number = Double.toString(Double.valueOf(left) / Double.valueOf(right));
        if (number.endsWith(".0"))
          number = number.replace(".0", "");
        return number;
      case MODULO:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance())
          return Double.toString(Double.valueOf(left) % Double.valueOf(right));
        else
          return Long.toString(Long.valueOf(left) % Long.valueOf(right));
      case AND:
        return Boolean.toString(left.equals("true") && right.equals("true"));
      case OR:
        return Boolean.toString(left.equals("true") || right.equals("true"));
      case EQ:
        return Boolean.toString(left.equals(right));
      case NE:
        return Boolean.toString(!left.equals(right));
      case LT:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
          return Boolean.toString(left.compareTo(right) < 0);
        else
          return Boolean.toString(Double.valueOf(left) < Double.valueOf(right));
      case LE:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
          return Boolean.toString(left.compareTo(right) <= 0);
        else
          return Boolean.toString(Double.valueOf(left) <= Double.valueOf(right));
      case GT:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
          return Boolean.toString(left.compareTo(right) > 0);
        else
          return Boolean.toString(Double.valueOf(left) > Double.valueOf(right));
      case GE:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance())
          return Boolean.toString(left.compareTo(right) >= 0);
        else
          return Boolean.toString(Double.valueOf(left) >= Double.valueOf(right));
      case PROPERTY_ACCESS:
        throw new ODataNotImplementedException();
      default:
        throw new ODataNotImplementedException();
      }

    case PROPERTY:
      final EdmProperty property = (EdmProperty) ((PropertyExpression) expression).getEdmProperty();
      final EdmSimpleType propertyType = (EdmSimpleType) property.getType();
      return propertyType.valueToString(getPropertyValue(data, property), EdmLiteralKind.DEFAULT, property.getFacets());

    case MEMBER:
      final MemberExpression memberExpression = (MemberExpression) expression;
      final PropertyExpression propertyExpression = (PropertyExpression) memberExpression.getProperty();
      final EdmProperty memberProperty = (EdmProperty) propertyExpression.getEdmProperty();
      final EdmSimpleType memberType = (EdmSimpleType) memberExpression.getEdmType();
      List<EdmProperty> propertyPath = new ArrayList<EdmProperty>();
      CommonExpression currentExpression = memberExpression;
      while (currentExpression.getKind() == ExpressionKind.MEMBER) {
        final MemberExpression currentMember = (MemberExpression) currentExpression;
        propertyPath.add(0, (EdmProperty) ((PropertyExpression) currentMember.getProperty()).getEdmProperty());
        currentExpression = currentMember.getPath();
      }
      propertyPath.add(0, (EdmProperty) ((PropertyExpression) currentExpression).getEdmProperty());
      return memberType.valueToString(getPropertyValue(data, propertyPath), EdmLiteralKind.DEFAULT, memberProperty.getFacets());

    case LITERAL:
      final LiteralExpression literal = (LiteralExpression) expression;
      final EdmSimpleType literalType = (EdmSimpleType) literal.getEdmType();
      return literalType.valueToString(literalType.valueOfString(literal.getUriLiteral(), EdmLiteralKind.URI, null, literalType.getDefaultType()),
          EdmLiteralKind.DEFAULT, null);

    case METHOD:
      final MethodExpression methodExpression = (MethodExpression) expression;
      final String first = evaluateExpression(data, methodExpression.getParameters().get(0));
      final String second = methodExpression.getParameterCount() > 1 ?
          evaluateExpression(data, methodExpression.getParameters().get(1)) : null;
      final String third = methodExpression.getParameterCount() > 2 ?
          evaluateExpression(data, methodExpression.getParameters().get(2)) : null;

      switch (methodExpression.getMethod()) {
      case ENDSWITH:
        return Boolean.toString(first.endsWith(second));
      case INDEXOF:
        return Integer.toString(first.indexOf(second));
      case STARTSWITH:
        return Boolean.toString(first.startsWith(second));
      case TOLOWER:
        return first.toLowerCase(Locale.ROOT);
      case TOUPPER:
        return first.toUpperCase(Locale.ROOT);
      case TRIM:
        return first.trim();
      case SUBSTRING:
        final int offset = first.indexOf(second);
        return first.substring(offset, offset + Integer.parseInt(third));
      case SUBSTRINGOF:
        return Boolean.toString(first.contains(second));
      case CONCAT:
        return first + second;
      case LENGTH:
        return Integer.toString(first.length());
      case YEAR:
        return String.valueOf(Integer.parseInt(first.substring(0, 4)));
      case MONTH:
        return String.valueOf(Integer.parseInt(first.substring(5, 7)));
      case DAY:
        return String.valueOf(Integer.parseInt(first.substring(8, 10)));
      case HOUR:
        return String.valueOf(Integer.parseInt(first.substring(11, 13)));
      case MINUTE:
        return String.valueOf(Integer.parseInt(first.substring(14, 16)));
      case SECOND:
        return String.valueOf(Integer.parseInt(first.substring(17, 19)));
      case ROUND:
        return Long.toString(Math.round(Double.valueOf(first)));
      case FLOOR:
        return Long.toString(Math.round(Math.floor(Double.valueOf(first))));
      case CEILING:
        return Long.toString(Math.round(Math.ceil(Double.valueOf(first))));
      default:
        throw new ODataNotImplementedException();
      }

    default:
      throw new ODataNotImplementedException();
    }
  }

  private static <T> String getSkipToken(final EdmEntitySet entitySet, final T data) throws ODataException {
    String skipToken = "";
    for (final EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      skipToken = skipToken.concat(type.valueToString(getPropertyValue(data, property), EdmLiteralKind.DEFAULT, property.getFacets()));
    }
    return skipToken;
  }

  private static <T> Object getPropertyValue(final T data, final List<EdmProperty> propertyPath) throws ODataException {
    Object dataObject = data;
    for (final EdmProperty property : propertyPath)
      if (dataObject != null)
        dataObject = getPropertyValue(dataObject, property);
    return dataObject;
  }

  private static <T> Object getPropertyValue(final T data, final EdmProperty property) throws ODataException {
    return getValue(data, getGetterMethodName(property));
  }

  private static <T, V> void setPropertyValue(final T data, final EdmProperty property, final V value) throws ODataException {
    final String methodName = getSetterMethodName(getGetterMethodName(property));
    if (methodName != null)
      setValue(data, methodName, value);
  }

  private static String getGetterMethodName(final EdmProperty property) throws EdmException {
    final String prefix = property.getType().getKind() == EdmTypeKind.SIMPLE && property.getType() == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance() ? "is" : "get";
    final String defaultMethodName = prefix + property.getName();
    return property.getMapping() == null || property.getMapping().getInternalName() == null ?
        defaultMethodName : property.getMapping().getInternalName();
  }

  private static String getSetterMethodName(final String getterMethodName) {
    if (getterMethodName.contains("."))
      return null;
    else
      return getterMethodName.replaceFirst("^is", "set").replaceFirst("^get", "set");
  }

  private <T> Map<String, Object> getStructuralTypeValueMap(final T data, final EdmStructuralType type) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "getStructuralTypeValueMap");

    Map<String, Object> valueMap = new HashMap<String, Object>();

    for (final String propertyName : type.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
      final Object value = getPropertyValue(data, property);

      if (property.isSimple())
        valueMap.put(propertyName, value);
      else
        valueMap.put(propertyName, getStructuralTypeValueMap(value, (EdmStructuralType) property.getType()));
    }

    context.stopRuntimeMeasurement(timingHandle);

    return valueMap;
  }

  private <T> void setStructuralTypeValuesFromMap(T data, final EdmStructuralType type, final Map<String, Object> valueMap, final boolean merge) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "setStructuralTypeValuesFromMap");

    for (final String propertyName : type.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
      if (type instanceof EdmEntityType && ((EdmEntityType) type).getKeyProperties().contains(property))
        continue;
      if (property.isSimple()) {
        final Object value = valueMap.get(propertyName);
        if (value != null || !merge)
          setPropertyValue(data, property, value);
      } else {
        @SuppressWarnings("unchecked")
        final Map<String, Object> values = (Map<String, Object>) valueMap.get(propertyName);
        if (values != null || !merge)
          setStructuralTypeValuesFromMap(getPropertyValue(data, property), (EdmStructuralType) property.getType(), values, merge);
      }
    }

    context.stopRuntimeMeasurement(timingHandle);
  }

  private static <T> Object getValue(final T data, final String methodName) throws ODataNotFoundException {
    Object dataObject = data;

    for (final String method : methodName.split("\\.", -1))
      if (dataObject != null)
        try {
          dataObject = dataObject.getClass().getMethod(method).invoke(dataObject);
        } catch (SecurityException e) {
          throw new ODataNotFoundException(null, e);
        } catch (NoSuchMethodException e) {
          throw new ODataNotFoundException(null, e);
        } catch (IllegalArgumentException e) {
          throw new ODataNotFoundException(null, e);
        } catch (IllegalAccessException e) {
          throw new ODataNotFoundException(null, e);
        } catch (InvocationTargetException e) {
          throw new ODataNotFoundException(null, e);
        }

    return dataObject;
  }

  private static <T, V> void setValue(final T data, final String methodName, final V value) throws ODataNotFoundException {
    try {
      boolean found = false;
      for (final Method method : Arrays.asList(data.getClass().getMethods()))
        if (method.getName().equals(methodName)) {
          found = true;
          // TODO: generalize type adaptation
          Object typedValue;
          Class<?> type = method.getParameterTypes()[0];
          if (value != null && type == Integer.class && value.getClass() == Short.class)
            typedValue = Integer.valueOf((Short) value);
          else
            typedValue = value;
          method.invoke(data, typedValue);
          break;
        }
      if (!found)
        throw new ODataNotFoundException(null);
    } catch (SecurityException e) {
      throw new ODataNotFoundException(null, e);
    } catch (IllegalArgumentException e) {
      throw new ODataNotFoundException(null, e);
    } catch (IllegalAccessException e) {
      throw new ODataNotFoundException(null, e);
    } catch (InvocationTargetException e) {
      throw new ODataNotFoundException(null, e);
    }
  }
}
