package com.sap.core.odata.ref.processor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmLiteralKind;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmSimpleTypeException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
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

/**
 * Implementation of the centralized parts of OData processing,
 * allowing to use the simplified {@link ListsDataSource}
 * for the actual data handling
 * @author SAP AG
 */
public class ListsProcessor extends ODataSingleProcessor {

  private static final int SERVER_PAGING_SIZE = 100;

  private static final String APPLICATION_XML = "application/xml";

  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

  private final ListsDataSource dataSource;

  public ListsProcessor(ListsDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ODataResponse readEntitySet(final GetEntitySetUriInfo uriParserResultView, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments()));

    final EdmEntitySet entitySet = uriParserResultView.getTargetEntitySet();
    final Integer count = applySystemQueryOptions(
        entitySet,
        data,
        uriParserResultView.getFilter(),
        uriParserResultView.getInlineCount(),
        uriParserResultView.getOrderBy(),
        uriParserResultView.getSkipToken(),
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    // Limit the number of returned entities and provide a "next" link
    // if there are further entities.
    // Almost all system query options in the current request must be carried
    // over to the URI for the "next" link, with the exception of $skiptoken
    // and $skip; this is missing currently.
    String nextSkipToken = null;
    if (data.size() > SERVER_PAGING_SIZE
        && uriParserResultView.getFilter() == null
        && uriParserResultView.getInlineCount() == null
        && uriParserResultView.getOrderBy() == null
        && uriParserResultView.getTop() == null
        && uriParserResultView.getExpand().isEmpty()
        && uriParserResultView.getSelect().isEmpty()) {
      if (uriParserResultView.getOrderBy() == null
          && uriParserResultView.getSkipToken() == null
          && uriParserResultView.getSkip() == null
          && uriParserResultView.getTop() == null) // applySystemQueryOptions did not sort
        sortInDefaultOrder(entitySet, data);
      nextSkipToken = getSkipToken(entitySet, data.get(SERVER_PAGING_SIZE));
      while (data.size() > SERVER_PAGING_SIZE)
        data.remove(SERVER_PAGING_SIZE);
    }

    final EdmEntityType entityType = entitySet.getEntityType();
    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
    for (final Object entryData : data)
      values.add(getStructuralTypeValueMap(entryData, entityType));

    final EntityProviderProperties feedProperties = EntityProviderProperties
        .baseUri(getContext().getUriInfo().getServiceRoot())
        .inlineCount(count)
        .skipToken(nextSkipToken)
        .inlineCountType(uriParserResultView.getInlineCount())
        .build();

    return ODataResponse.fromResponse(EntityProvider.create(contentType).writeFeed(uriParserResultView.getTargetEntitySet(), values, feedProperties))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse countEntitySet(final GetEntitySetCountUriInfo uriParserResultView, final String contentType) throws ODataException {
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
        uriParserResultView.getFilter(),
        null,
        null,
        null,
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    return ODataResponse.fromResponse(EntityProvider.create(APPLICATION_XML).writeText(String.valueOf(data.size())))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse readEntityLinks(final GetEntitySetLinksUriInfo uriParserResultView, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    data.addAll((List<?>) retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments()));

    final Integer count = applySystemQueryOptions(
        uriParserResultView.getTargetEntitySet(),
        data,
        uriParserResultView.getFilter(),
        uriParserResultView.getInlineCount(),
        // uriParserResultView.getOrderBy(),
        null,
        uriParserResultView.getSkipToken(),
        uriParserResultView.getSkip(),
        uriParserResultView.getTop());

    final EdmEntitySet entitySet = uriParserResultView.getTargetEntitySet();

    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
    for (final Object entryData : data) {
      Map<String, Object> entryValues = new HashMap<String, Object>();
      for (final EdmProperty property : entitySet.getEntityType().getKeyProperties())
        entryValues.put(property.getName(), getPropertyValue(entryData, property));
      values.add(entryValues);
    }

    final EntityProviderProperties entryProperties = EntityProviderProperties
        .baseUri(getContext().getUriInfo().getServiceRoot()).inlineCount(count).build();

    return ODataResponse.fromResponse(EntityProvider.create(contentType).writeLinks(entitySet, values, entryProperties))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse countEntityLinks(final GetEntitySetLinksCountUriInfo uriParserResultView, final String contentType) throws ODataException {
    return countEntitySet((GetEntitySetCountUriInfo) uriParserResultView, contentType);
  }

  @Override
  public ODataResponse readEntity(final GetEntityUriInfo uriParserResultView, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    if (!appliesFilter(data, uriParserResultView.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriParserResultView.getTargetEntitySet();
    final Map<String, Object> values = getStructuralTypeValueMap(data, entitySet.getEntityType());

    final EntityProviderProperties entryProperties = EntityProviderProperties
        .baseUri(getContext().getUriInfo().getServiceRoot()).build();

    return ODataResponse.fromResponse(EntityProvider.create(contentType).writeEntry(entitySet, values, entryProperties))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse existsEntity(final GetEntityCountUriInfo uriParserResultView, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    return ODataResponse.fromResponse(EntityProvider.create(APPLICATION_XML).writeText(
        appliesFilter(data, uriParserResultView.getFilter()) ? "1" : "0"))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse deleteEntity(final DeleteUriInfo uriParserResultView, final String contentType) throws ODataException {
    dataSource.deleteData(
        uriParserResultView.getStartEntitySet(),
        mapKey(uriParserResultView.getKeyPredicates()));
    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse readEntityLink(final GetEntityLinkUriInfo uriParserResultView, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    // if (!appliesFilter(data, uriParserResultView.getFilter()))
    if (data == null)
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriParserResultView.getTargetEntitySet();

    Map<String, Object> values = new HashMap<String, Object>();
    for (final EdmProperty property : entitySet.getEntityType().getKeyProperties())
      values.put(property.getName(), getPropertyValue(data, property));

    final EntityProviderProperties entryProperties = EntityProviderProperties
        .baseUri(getContext().getUriInfo().getServiceRoot()).build();

    return ODataResponse.fromResponse(EntityProvider.create(contentType).writeLink(entitySet, values, entryProperties))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse existsEntityLink(final GetEntityLinkCountUriInfo uriParserResultView, final String contentType) throws ODataException {
    return existsEntity((GetEntityCountUriInfo) uriParserResultView, contentType);
  }

  @Override
  public ODataResponse deleteEntityLink(final DeleteUriInfo uriParserResultView, final String contentType) throws ODataException {
    final List<NavigationSegment> navigationSegments = uriParserResultView.getNavigationSegments();
    final List<NavigationSegment> previousSegments = navigationSegments.subList(0, navigationSegments.size() - 1);

    final Object sourceData = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        previousSegments);

    EdmEntitySet entitySet;
    if (previousSegments.isEmpty())
      entitySet = uriParserResultView.getStartEntitySet();
    else
      entitySet = previousSegments.get(previousSegments.size() - 1).getEntitySet();
    final NavigationSegment navigationSegment = navigationSegments.get(navigationSegments.size() - 1);
    final HashMap<String, Object> keys = mapKey(navigationSegment.getKeyPredicates());
    final Object targetData = dataSource.readRelatedData(
        entitySet, sourceData, navigationSegment.getEntitySet(), keys);

    // if (!appliesFilter(targetData, uriParserResultView.getFilter()))
    if (targetData == null)
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    dataSource.deleteRelation(entitySet, sourceData, navigationSegment.getEntitySet(), keys);
    return ODataResponse.status(HttpStatusCodes.NO_CONTENT).build();
  }

  @Override
  public ODataResponse readEntityComplexProperty(final GetComplexPropertyUriInfo uriParserResultView, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    // if (!appliesFilter(data, uriParserResultView.getFilter()))
    //   throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriParserResultView.getPropertyPath();
    data = getPropertyValue(data, propertyPath);

    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    final EdmType type = property.getType();

    final Object value = type.getKind() == EdmTypeKind.COMPLEX ?
        getStructuralTypeValueMap(data, (EdmStructuralType) type) : data;

    return ODataResponse.fromResponse(EntityProvider.create(contentType).writeProperty(property, value))
        .status(HttpStatusCodes.OK)
        .build();

  }

  @Override
  public ODataResponse readEntitySimpleProperty(final GetSimplePropertyUriInfo uriParserResultView, final String contentType) throws ODataException {
    return readEntityComplexProperty((GetComplexPropertyUriInfo) uriParserResultView, contentType);
  }

  @Override
  public ODataResponse readEntitySimplePropertyValue(final GetSimplePropertyUriInfo uriParserResultView, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    // if (!appliesFilter(data, uriParserResultView.getFilter()))
    //   throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final List<EdmProperty> propertyPath = uriParserResultView.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    Object value = getPropertyValue(data, propertyPath);

    if (property.getMapping() != null && property.getMapping().getMimeType() != null) {
      Map<String, Object> valueWithMimeType = new HashMap<String, Object>();
      valueWithMimeType.put(property.getName(), value);
      final String mimeTypeMappingName = property.getMapping().getMimeType();
      valueWithMimeType.put(mimeTypeMappingName, getValue(data, mimeTypeMappingName));
      value = valueWithMimeType;
    }

    return ODataResponse.fromResponse(EntityProvider.create(APPLICATION_XML).writePropertyValue(property, value))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse readEntityMedia(final GetMediaResourceUriInfo uriParserResultView, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriParserResultView.getStartEntitySet(),
        uriParserResultView.getKeyPredicates(),
        uriParserResultView.getFunctionImport(),
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        uriParserResultView.getNavigationSegments());

    if (!appliesFilter(data, uriParserResultView.getFilter()))
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    final EdmEntitySet entitySet = uriParserResultView.getTargetEntitySet();
    StringBuilder mimeTypeBuilder = new StringBuilder();
    final byte[] binaryData = dataSource.readBinaryData(entitySet, data, mimeTypeBuilder);

    final String mimeType = mimeTypeBuilder.toString().isEmpty() ?
        APPLICATION_OCTET_STREAM : mimeTypeBuilder.toString();

    return ODataResponse.fromResponse(EntityProvider.create(APPLICATION_XML).writeBinary(mimeType, binaryData))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse executeFunctionImport(final GetFunctionImportUriInfo uriParserResultView, final String contentType) throws ODataException {
    final EdmFunctionImport functionImport = uriParserResultView.getFunctionImport();
    final EdmType type = functionImport.getReturnType().getType();

    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
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

    final EntityProviderProperties entryProperties = EntityProviderProperties
        .baseUri(getContext().getUriInfo().getServiceRoot()).build();

    return ODataResponse.fromResponse(EntityProvider.create(contentType).writeFunctionImport(functionImport, value, entryProperties))
        .status(HttpStatusCodes.OK)
        .build();
  }

  @Override
  public ODataResponse executeFunctionImportValue(final GetFunctionImportUriInfo uriParserResultView, final String contentType) throws ODataException {
    final EdmFunctionImport functionImport = uriParserResultView.getFunctionImport();
    final EdmSimpleType type = (EdmSimpleType) functionImport.getReturnType().getType();

    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriParserResultView.getFunctionImportParameters()),
        null);

    if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
      return ODataResponse.fromResponse(EntityProvider.create(APPLICATION_XML)
          .writeBinary(APPLICATION_OCTET_STREAM, (byte[]) data))
          .status(HttpStatusCodes.OK)
          .build();
    } else {
      final String value = type.valueToString(data, EdmLiteralKind.DEFAULT, null);
      return ODataResponse.fromResponse(EntityProvider.create(APPLICATION_XML)
          .writeText(value == null ? "" : value))
          .status(HttpStatusCodes.OK)
          .build();
    }
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
        parameterMap.put(parameterName, literal.getType().valueOfString(literal.getLiteral(), EdmLiteralKind.DEFAULT, null));
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

  private <T> Integer applySystemQueryOptions(final EdmEntitySet entitySet, List<T> data, final FilterExpression filter, final InlineCount inlineCount, final OrderByExpression orderBy, final String skipToken, final Integer skip, final Integer top) throws ODataException {
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

    return count;
  }

  private <T> void sort(List<T> data, final OrderByExpression orderBy) {
    Collections.sort(data, new Comparator<T>() {
      @Override
      public int compare(final T entity1, final T entity2) {
        try {
          int result = 0;
          for (OrderExpression expression : orderBy.getOrders()) {
            result = evaluateExpression(entity1, expression.getExpression()).compareTo(
                evaluateExpression(entity2, expression.getExpression()));
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

  private <T> void sortInDefaultOrder(final EdmEntitySet entitySet, List<T> data) {
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
    else
      try {
        return evaluateExpression(data, filter.getExpression()).equals("true");
      } catch (RuntimeException e) {
        return false;
      }
  }

  private <T> String evaluateExpression(final T data, final CommonExpression expression) throws ODataException {
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
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance())
          return Double.toString(Double.valueOf(left) / Double.valueOf(right));
        else
          return Long.toString(Long.valueOf(left) / Long.valueOf(right));
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
      while (currentExpression != null && currentExpression.getKind() == ExpressionKind.MEMBER) {
        final MemberExpression currentMember = (MemberExpression) currentExpression;
        propertyPath.add(0, (EdmProperty) ((PropertyExpression) currentMember.getProperty()).getEdmProperty());
        currentExpression = currentMember.getPath();
      }
      propertyPath.add(0, (EdmProperty) ((PropertyExpression) currentExpression).getEdmProperty());
      return memberType.valueToString(getPropertyValue(data, propertyPath), EdmLiteralKind.DEFAULT, memberProperty.getFacets());

    case LITERAL:
      final LiteralExpression literal = (LiteralExpression) expression;
      final EdmSimpleType literalType = (EdmSimpleType) literal.getEdmType();
      return literalType.valueToString(literalType.valueOfString(literal.getUriLiteral(), EdmLiteralKind.URI, null), EdmLiteralKind.DEFAULT, null);

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

  private <T> String getSkipToken(final EdmEntitySet entitySet, final T data) throws ODataException {
    String skipToken = "";
    for (final EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
      final EdmSimpleType type = (EdmSimpleType) property.getType();
      skipToken = skipToken.concat(type.valueToString(getPropertyValue(data, property), EdmLiteralKind.DEFAULT, property.getFacets()));
    }
    return skipToken;
  }

  private <T> Object getPropertyValue(final T data, final List<EdmProperty> propertyPath) throws ODataException {
    Object dataObject = data;
    for (final EdmProperty property : propertyPath)
      if (dataObject != null)
        dataObject = getPropertyValue(dataObject, property);
    return dataObject;
  }

  private <T> Object getPropertyValue(final T data, final EdmProperty property) throws ODataException {
    final String prefix = property.getType().getKind() == EdmTypeKind.SIMPLE && property.getType() == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance() ? "is" : "get";
    final String defaultMethodName = prefix + property.getName();
    final String methodName = property.getMapping() == null || property.getMapping().getInternalName() == null ?
        defaultMethodName : property.getMapping().getInternalName();

    return getValue(data, methodName);
  }

  private <T> Map<String, Object> getStructuralTypeValueMap(final T data, final EdmStructuralType type) throws ODataException {
    Map<String, Object> valueMap = new HashMap<String, Object>();

    for (final String propertyName : type.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
      final Object value = getPropertyValue(data, property);

      if (property.getType().getKind() == EdmTypeKind.COMPLEX)
        valueMap.put(propertyName, getStructuralTypeValueMap(value, (EdmStructuralType) property.getType()));
      else
        valueMap.put(propertyName, value);
    }

    return valueMap;
  }

  private <T> Object getValue(final T data, final String methodName) throws ODataNotFoundException {
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

}
