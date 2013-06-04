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

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmEntityContainer;
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
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.EntityProvider;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.callback.OnWriteEntryContent;
import com.sap.core.odata.api.ep.callback.OnWriteFeedContent;
import com.sap.core.odata.api.ep.callback.WriteCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
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
 * allowing to use the simplified {@link ListsDataSource} for the
 * actual data handling.
 * @author SAP AG
 */
public class ListsProcessor extends ODataSingleProcessor {

  private static final int SERVER_PAGING_SIZE = 100;

  private final ListsDataSource dataSource;

  public ListsProcessor(final ListsDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public ODataResponse readEntitySet(final GetEntitySetUriInfo uriInfo, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    try {
      data.addAll((List<?>) retrieveData(
          uriInfo.getStartEntitySet(),
          uriInfo.getKeyPredicates(),
          uriInfo.getFunctionImport(),
          mapFunctionParameters(uriInfo.getFunctionImportParameters()),
          uriInfo.getNavigationSegments()));
    } catch (final ODataNotFoundException e) {
      data.clear();
    }

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
    // and $skip; currently, this is done only for $inlinecount. If one of
    // the not supported system query options is present, paging does not take place.
    String nextLink = null;
    if (data.size() > SERVER_PAGING_SIZE
        && uriInfo.getFilter() == null
        /*
         * Take orderby into account of next link. 
         * Actually there is no sorting implemented yet. 
         */
        && uriInfo.getTop() == null
        && uriInfo.getExpand().isEmpty()
        && uriInfo.getSelect().isEmpty()) {
      if (uriInfo.getOrderBy() == null
          && uriInfo.getSkipToken() == null
          && uriInfo.getSkip() == null
          && uriInfo.getTop() == null) {
        sortInDefaultOrder(entitySet, data);
      }
      final EdmEntityContainer entityContainer = entitySet.getEntityContainer();
      // TODO: Percent-encode "next" link and add navigation path
      nextLink = (entityContainer.isDefaultEntityContainer() ? "" : entityContainer.getName() + Edm.DELIMITER)
          + entitySet.getName()
          + "?$skiptoken=" + getSkipToken(entitySet, data.get(SERVER_PAGING_SIZE))
          + (inlineCountType == null ? "" : "&$inlinecount=" + inlineCountType.toString().toLowerCase(Locale.ROOT));
      if (uriInfo.getOrderBy() != null) {
        nextLink += "&$orderby=" + uriInfo.getOrderBy().getUriLiteral();
      }

      while (data.size() > SERVER_PAGING_SIZE) {
        data.remove(SERVER_PAGING_SIZE);
      }
    }

    final EdmEntityType entityType = entitySet.getEntityType();
    List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
    for (final Object entryData : data) {
      values.add(getStructuralTypeValueMap(entryData, entityType));
    }

    ODataContext context = getContext();
    final EntityProviderWriteProperties feedProperties = EntityProviderWriteProperties
        .serviceRoot(context.getPathInfo().getServiceRoot())
        .inlineCountType(inlineCountType)
        .inlineCount(count)
        .expandSelectTree(UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand()))
        .callbacks(getCallbacks(data, entityType))
        .nextLink(nextLink)
        .build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeFeed");

    final ODataResponse response = EntityProvider.writeFeed(contentType, entitySet, values, feedProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).build();
  }

  @Override
  public ODataResponse countEntitySet(final GetEntitySetCountUriInfo uriInfo, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    try {
      data.addAll((List<?>) retrieveData(
          uriInfo.getStartEntitySet(),
          uriInfo.getKeyPredicates(),
          uriInfo.getFunctionImport(),
          mapFunctionParameters(uriInfo.getFunctionImportParameters()),
          uriInfo.getNavigationSegments()));
    } catch (final ODataNotFoundException e) {
      data.clear();
    }

    applySystemQueryOptions(
        uriInfo.getTargetEntitySet(),
        data,
        uriInfo.getFilter(),
        null,
        null,
        null,
        uriInfo.getSkip(),
        uriInfo.getTop());

    return ODataResponse.fromResponse(EntityProvider.writeText(String.valueOf(data.size()))).build();
  }

  @Override
  public ODataResponse readEntityLinks(final GetEntitySetLinksUriInfo uriInfo, final String contentType) throws ODataException {
    ArrayList<Object> data = new ArrayList<Object>();
    try {
      data.addAll((List<?>) retrieveData(
          uriInfo.getStartEntitySet(),
          uriInfo.getKeyPredicates(),
          uriInfo.getFunctionImport(),
          mapFunctionParameters(uriInfo.getFunctionImportParameters()),
          uriInfo.getNavigationSegments()));
    } catch (final ODataNotFoundException e) {
      data.clear();
    }

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
      for (final EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
        entryValues.put(property.getName(), getPropertyValue(entryData, property));
      }
      values.add(entryValues);
    }

    ODataContext context = getContext();
    final EntityProviderWriteProperties entryProperties = EntityProviderWriteProperties
        .serviceRoot(context.getPathInfo().getServiceRoot())
        .inlineCountType(uriInfo.getInlineCount())
        .inlineCount(count)
        .build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeLinks");

    final ODataResponse response = EntityProvider.writeLinks(contentType, entitySet, values, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).build();
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

    if (!appliesFilter(data, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final ExpandSelectTreeNode expandSelectTreeNode = UriParser.createExpandSelectTree(uriInfo.getSelect(), uriInfo.getExpand());
    return ODataResponse.fromResponse(writeEntry(uriInfo.getTargetEntitySet(), expandSelectTreeNode, data, contentType)).build();
  }

  @Override
  public ODataResponse existsEntity(final GetEntityCountUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    return ODataResponse.fromResponse(EntityProvider.writeText(appliesFilter(data, uriInfo.getFilter()) ? "1" : "0")).build();
  }

  @Override
  public ODataResponse deleteEntity(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    dataSource.deleteData(
        uriInfo.getStartEntitySet(),
        mapKey(uriInfo.getKeyPredicates()));
    return ODataResponse.newBuilder().build();
  }

  @Override
  public ODataResponse createEntity(final PostUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    if (!uriInfo.getNavigationSegments().isEmpty()) {
      throw new ODataNotImplementedException(ODataNotImplementedException.COMMON);
    }

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final EdmEntityType entityType = entitySet.getEntityType();

    Object data = dataSource.newDataObject(entitySet);
    ExpandSelectTreeNode expandSelectTree = null;

    if (entityType.hasStream()) {
      dataSource.createData(entitySet, data);
      dataSource.writeBinaryData(entitySet, data,
          new BinaryData(EntityProvider.readBinary(content), requestContentType));

    } else {
      final EntityProviderReadProperties properties = EntityProviderReadProperties.init()
          .mergeSemantic(true)
          .addTypeMappings(getStructuralTypeTypeMap(data, entityType))
          .build();
      final ODataEntry entryValues = parseEntry(entitySet, content, requestContentType, properties);

      setStructuralTypeValuesFromMap(data, entityType, entryValues.getProperties(), true);

      dataSource.createData(entitySet, data);

      createInlinedEntities(entitySet, data, entryValues);

      expandSelectTree = entryValues.getExpandSelectTree();
    }

    return ODataResponse.fromResponse(writeEntry(uriInfo.getTargetEntitySet(), expandSelectTree, data, contentType)).eTag(constructETag(entitySet, data)).build();
  }

  @Override
  public ODataResponse updateEntity(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final boolean merge, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final EdmEntityType entityType = entitySet.getEntityType();
    final EntityProviderReadProperties properties = EntityProviderReadProperties.init()
        .mergeSemantic(merge)
        .addTypeMappings(getStructuralTypeTypeMap(data, entityType))
        .build();
    final ODataEntry entryValues = parseEntry(entitySet, content, requestContentType, properties);

    setStructuralTypeValuesFromMap(data, entityType, entryValues.getProperties(), merge);

    return ODataResponse.newBuilder().eTag(constructETag(entitySet, data)).build();
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
    if (data == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();

    Map<String, Object> values = new HashMap<String, Object>();
    for (final EdmProperty property : entitySet.getEntityType().getKeyProperties()) {
      values.put(property.getName(), getPropertyValue(data, property));
    }

    ODataContext context = getContext();
    final EntityProviderWriteProperties entryProperties = EntityProviderWriteProperties
        .serviceRoot(context.getPathInfo().getServiceRoot())
        .build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeLink");

    final ODataResponse response = EntityProvider.writeLink(contentType, entitySet, values, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).build();
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

    final EdmEntitySet entitySet = previousSegments.isEmpty() ?
        uriInfo.getStartEntitySet() : previousSegments.get(previousSegments.size() - 1).getEntitySet();

    final NavigationSegment navigationSegment = navigationSegments.get(navigationSegments.size() - 1);
    final Map<String, Object> keys = mapKey(navigationSegment.getKeyPredicates());

    final Object targetData = dataSource.readRelatedData(
        entitySet, sourceData, navigationSegment.getEntitySet(), keys);

    // if (!appliesFilter(targetData, uriInfo.getFilter()))
    if (targetData == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    dataSource.deleteRelation(entitySet, sourceData, navigationSegment.getEntitySet(), keys);

    return ODataResponse.newBuilder().build();
  }

  @Override
  public ODataResponse createEntityLink(final PostUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    final List<NavigationSegment> navigationSegments = uriInfo.getNavigationSegments();
    final List<NavigationSegment> previousSegments = navigationSegments.subList(0, navigationSegments.size() - 1);

    final Object sourceData = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        previousSegments);

    final EdmEntitySet entitySet = previousSegments.isEmpty() ?
        uriInfo.getStartEntitySet() : previousSegments.get(previousSegments.size() - 1).getEntitySet();
    final EdmEntitySet targetEntitySet = uriInfo.getTargetEntitySet();

    final Map<String, Object> targetKeys = parseLink(targetEntitySet, content, requestContentType);

    dataSource.writeRelation(entitySet, sourceData, targetEntitySet, targetKeys);

    return ODataResponse.newBuilder().build();
  }

  @Override
  public ODataResponse updateEntityLink(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    final List<NavigationSegment> navigationSegments = uriInfo.getNavigationSegments();
    final List<NavigationSegment> previousSegments = navigationSegments.subList(0, navigationSegments.size() - 1);

    final Object sourceData = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        previousSegments);

    final EdmEntitySet entitySet = previousSegments.isEmpty() ?
        uriInfo.getStartEntitySet() : previousSegments.get(previousSegments.size() - 1).getEntitySet();
    final NavigationSegment navigationSegment = navigationSegments.get(navigationSegments.size() - 1);
    final Map<String, Object> keys = mapKey(navigationSegment.getKeyPredicates());
    final EdmEntitySet targetEntitySet = uriInfo.getTargetEntitySet();

    final Object targetData = dataSource.readRelatedData(entitySet, sourceData, targetEntitySet, keys);

    if (!appliesFilter(targetData, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    dataSource.deleteRelation(entitySet, sourceData, targetEntitySet, keys);

    final Map<String, Object> newKeys = parseLink(targetEntitySet, content, requestContentType);

    dataSource.writeRelation(entitySet, sourceData, targetEntitySet, newKeys);

    return ODataResponse.newBuilder().build();
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
    if (data == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    final Object value = property.isSimple() ?
        property.getMapping() == null || property.getMapping().getMimeType() == null ?
            getPropertyValue(data, propertyPath) : getSimpleTypeValueMap(data, propertyPath) :
        getStructuralTypeValueMap(getPropertyValue(data, propertyPath), (EdmStructuralType) property.getType());

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeProperty");

    final ODataResponse response = EntityProvider.writeProperty(contentType, property, value);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).eTag(constructETag(uriInfo.getTargetEntitySet(), data)).build();
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
    if (data == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    final Object value = property.getMapping() == null || property.getMapping().getMimeType() == null ?
        getPropertyValue(data, propertyPath) : getSimpleTypeValueMap(data, propertyPath);

    return ODataResponse.fromResponse(EntityProvider.writePropertyValue(property, value)).eTag(constructETag(uriInfo.getTargetEntitySet(), data)).build();
  }

  @Override
  public ODataResponse deleteEntitySimplePropertyValue(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (data == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    data = getPropertyValue(data, propertyPath.subList(0, propertyPath.size() - 1));
    setPropertyValue(data, property, null);
    if (property.getMapping() != null && property.getMapping().getMimeType() != null) {
      setValue(data, getSetterMethodName(property.getMapping().getMimeType()), null);
    }

    return ODataResponse.newBuilder().build();
  }

  @Override
  public ODataResponse updateEntityComplexProperty(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final boolean merge, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    data = getPropertyValue(data, propertyPath.subList(0, propertyPath.size() - 1));

    ODataContext context = getContext();
    int timingHandle = context.startRuntimeMeasurement("EntityConsumer", "readProperty");

    Map<String, Object> values;
    try {
      values = EntityProvider.readProperty(requestContentType, property, content, EntityProviderReadProperties.init().mergeSemantic(merge).build());
    } catch (final EntityProviderException e) {
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

    return ODataResponse.newBuilder().eTag(constructETag(uriInfo.getTargetEntitySet(), data)).build();
  }

  @Override
  public ODataResponse updateEntitySimpleProperty(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    return updateEntityComplexProperty(uriInfo, content, requestContentType, false, contentType);
  }

  @Override
  public ODataResponse updateEntitySimplePropertyValue(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);

    data = getPropertyValue(data, propertyPath.subList(0, propertyPath.size() - 1));

    ODataContext context = getContext();
    int timingHandle = context.startRuntimeMeasurement("EntityConsumer", "readPropertyValue");

    Object value;
    try {
      value = EntityProvider.readPropertyValue(property, content);
    } catch (final EntityProviderException e) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
    }

    context.stopRuntimeMeasurement(timingHandle);

    setPropertyValue(data, property, value);
    if (property.getMapping() != null && property.getMapping().getMimeType() != null) {
      setValue(data, getSetterMethodName(property.getMapping().getMimeType()), requestContentType);
    }

    return ODataResponse.newBuilder().eTag(constructETag(uriInfo.getTargetEntitySet(), data)).build();
  }

  @Override
  public ODataResponse readEntityMedia(final GetMediaResourceUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    final BinaryData binaryData = dataSource.readBinaryData(entitySet, data);
    if (binaryData == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    final String mimeType = binaryData.getMimeType() == null ?
        HttpContentType.APPLICATION_OCTET_STREAM : binaryData.getMimeType();

    return ODataResponse.fromResponse(EntityProvider.writeBinary(mimeType, binaryData.getData())).eTag(constructETag(entitySet, data)).build();
  }

  @Override
  public ODataResponse deleteEntityMedia(final DeleteUriInfo uriInfo, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (data == null) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    dataSource.writeBinaryData(uriInfo.getTargetEntitySet(), data, new BinaryData(null, null));

    return ODataResponse.newBuilder().build();
  }

  @Override
  public ODataResponse updateEntityMedia(final PutMergePatchUriInfo uriInfo, final InputStream content, final String requestContentType, final String contentType) throws ODataException {
    final Object data = retrieveData(
        uriInfo.getStartEntitySet(),
        uriInfo.getKeyPredicates(),
        uriInfo.getFunctionImport(),
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        uriInfo.getNavigationSegments());

    if (!appliesFilter(data, uriInfo.getFilter())) {
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
    }

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "readBinary");

    final byte[] value = EntityProvider.readBinary(content);

    context.stopRuntimeMeasurement(timingHandle);

    final EdmEntitySet entitySet = uriInfo.getTargetEntitySet();
    dataSource.writeBinaryData(entitySet, data, new BinaryData(value, requestContentType));

    return ODataResponse.newBuilder().eTag(constructETag(entitySet, data)).build();
  }

  @Override
  public ODataResponse executeFunctionImport(final GetFunctionImportUriInfo uriInfo, final String contentType) throws ODataException {
    final EdmFunctionImport functionImport = uriInfo.getFunctionImport();
    final EdmType type = functionImport.getReturnType().getType();

    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        null);

    if (data == null) {
      throw new ODataNotFoundException(ODataHttpException.COMMON);
    }

    Object value;
    if (type.getKind() == EdmTypeKind.SIMPLE) {
      value = type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance() ?
          ((BinaryData) data).getData() : data;
    } else if (functionImport.getReturnType().getMultiplicity() == EdmMultiplicity.MANY) {
      List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
      for (final Object typeData : (List<?>) data) {
        values.add(getStructuralTypeValueMap(typeData, (EdmStructuralType) type));
      }
      value = values;
    } else {
      value = getStructuralTypeValueMap(data, (EdmStructuralType) type);
    }

    ODataContext context = getContext();
    final EntityProviderWriteProperties entryProperties = EntityProviderWriteProperties
        .serviceRoot(context.getPathInfo().getServiceRoot()).build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeFunctionImport");

    final ODataResponse response = EntityProvider.writeFunctionImport(contentType, functionImport, value, entryProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return ODataResponse.fromResponse(response).build();
  }

  @Override
  public ODataResponse executeFunctionImportValue(final GetFunctionImportUriInfo uriInfo, final String contentType) throws ODataException {
    final EdmFunctionImport functionImport = uriInfo.getFunctionImport();
    final EdmSimpleType type = (EdmSimpleType) functionImport.getReturnType().getType();

    final Object data = dataSource.readData(
        functionImport,
        mapFunctionParameters(uriInfo.getFunctionImportParameters()),
        null);

    if (data == null) {
      throw new ODataNotFoundException(ODataHttpException.COMMON);
    }

    ODataResponse response;
    if (type == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance()) {
      response = EntityProvider.writeBinary(((BinaryData) data).getMimeType(), ((BinaryData) data).getData());
    } else {
      final String value = type.valueToString(data, EdmLiteralKind.DEFAULT, null);
      response = EntityProvider.writeText(value == null ? "" : value);
    }
    return ODataResponse.fromResponse(response).build();
  }

  private static Map<String, Object> mapKey(final List<KeyPredicate> keys) throws EdmException {
    Map<String, Object> keyMap = new HashMap<String, Object>();
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
      Map<String, Object> parameterMap = new HashMap<String, Object>();
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

    data = functionImport == null ?
        keys.isEmpty() ?
            dataSource.readData(startEntitySet) : dataSource.readData(startEntitySet, keys) :
        dataSource.readData(functionImport, functionImportParameters, keys);

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

  private <T> String constructETag(final EdmEntitySet entitySet, final T data) throws ODataException {
    final EdmEntityType entityType = entitySet.getEntityType();
    String eTag = null;
    for (final String propertyName : entityType.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) entityType.getProperty(propertyName);
      if (property.getFacets() != null && property.getFacets().getConcurrencyMode() == EdmConcurrencyMode.Fixed) {
        final EdmSimpleType type = (EdmSimpleType) property.getType();
        final String component = type.valueToString(getPropertyValue(data, property), EdmLiteralKind.DEFAULT, property.getFacets());
        eTag = eTag == null ? component : eTag + Edm.DELIMITER + component;
      }
    }
    return eTag == null ? null : "W/\"" + eTag + "\"";
  }

  private <T> Map<String, ODataCallback> getCallbacks(final T data, final EdmEntityType entityType) throws EdmException {
    final List<String> navigationPropertyNames = entityType.getNavigationPropertyNames();
    if (navigationPropertyNames.isEmpty()) {
      return null;
    } else {
      final WriteCallback callback = new WriteCallback(data);
      Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
      for (final String name : navigationPropertyNames) {
        callbacks.put(name, callback);
      }
      return callbacks;
    }
  }

  private class WriteCallback implements OnWriteEntryContent, OnWriteFeedContent {
    private final Object data;

    private <T> WriteCallback(final T data) {
      this.data = data;
    }

    @Override
    public WriteFeedCallbackResult retrieveFeedResult(final WriteFeedCallbackContext context) throws ODataApplicationException {
      try {
        final EdmEntityType entityType = context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType();
        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        Object relatedData = null;
        try {
          relatedData = readRelatedData(context);
          for (final Object entryData : (List<?>) relatedData) {
            values.add(getStructuralTypeValueMap(entryData, entityType));
          }
        } catch (final ODataNotFoundException e) {
          values.clear();
        }
        WriteFeedCallbackResult result = new WriteFeedCallbackResult();
        result.setFeedData(values);
        EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).callbacks(getCallbacks(relatedData, entityType)).expandSelectTree(context.getCurrentExpandSelectTreeNode()).selfLink(context.getSelfLink()).build();
        result.setInlineProperties(inlineProperties);
        return result;
      } catch (final ODataException e) {
        throw new ODataApplicationException(e.getLocalizedMessage(), Locale.ROOT, e);
      }
    }

    @Override
    public WriteEntryCallbackResult retrieveEntryResult(final WriteEntryCallbackContext context) throws ODataApplicationException {
      try {
        final EdmEntityType entityType = context.getSourceEntitySet().getRelatedEntitySet(context.getNavigationProperty()).getEntityType();
        WriteEntryCallbackResult result = new WriteEntryCallbackResult();
        Object relatedData;
        try {
          relatedData = readRelatedData(context);
        } catch (final ODataNotFoundException e) {
          relatedData = null;
        }
        result.setEntryData(getStructuralTypeValueMap(relatedData, entityType));
        EntityProviderWriteProperties inlineProperties = EntityProviderWriteProperties.serviceRoot(getContext().getPathInfo().getServiceRoot()).callbacks(getCallbacks(relatedData, entityType)).expandSelectTree(context.getCurrentExpandSelectTreeNode()).build();
        result.setInlineProperties(inlineProperties);
        return result;
      } catch (final ODataException e) {
        throw new ODataApplicationException(e.getLocalizedMessage(), Locale.ROOT, e);
      }
    }

    private Object readRelatedData(final WriteCallbackContext context) throws ODataException {
      final EdmEntitySet entitySet = context.getSourceEntitySet();
      return dataSource.readRelatedData(
          entitySet,
          data instanceof List ? readEntryData((List<?>) data, entitySet.getEntityType(), context.extractKeyFromEntryData()) : data,
          entitySet.getRelatedEntitySet(context.getNavigationProperty()),
          Collections.<String, Object> emptyMap());
    }

    private <T> T readEntryData(final List<T> data, final EdmEntityType entityType, final Map<String, Object> key) throws ODataException {
      for (final T entryData : data) {
        boolean found = true;
        for (final EdmProperty keyProperty : entityType.getKeyProperties()) {
          if (!getPropertyValue(entryData, keyProperty).equals(key.get(keyProperty.getName()))) {
            found = false;
            break;
          }
        }
        if (found) {
          return entryData;
        }
      }
      return null;
    }
  }

  private <T> ODataResponse writeEntry(final EdmEntitySet entitySet, final ExpandSelectTreeNode expandSelectTree, final T data, final String contentType) throws ODataException, EntityProviderException {
    final EdmEntityType entityType = entitySet.getEntityType();
    final Map<String, Object> values = getStructuralTypeValueMap(data, entityType);

    ODataContext context = getContext();
    EntityProviderWriteProperties writeProperties = EntityProviderWriteProperties
        .serviceRoot(context.getPathInfo().getServiceRoot())
        .expandSelectTree(expandSelectTree)
        .callbacks(getCallbacks(data, entityType))
        .build();

    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "writeEntry");

    final ODataResponse response = EntityProvider.writeEntry(contentType, entitySet, values, writeProperties);

    context.stopRuntimeMeasurement(timingHandle);

    return response;
  }

  private ODataEntry parseEntry(final EdmEntitySet entitySet, final InputStream content, final String requestContentType, final EntityProviderReadProperties properties) throws ODataBadRequestException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityConsumer", "readEntry");

    ODataEntry entryValues;
    try {
      entryValues = EntityProvider.readEntry(requestContentType, entitySet, content, properties);
    } catch (final EntityProviderException e) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY, e);
    }

    context.stopRuntimeMeasurement(timingHandle);

    return entryValues;
  }

  private Map<String, Object> parseLink(final EdmEntitySet entitySet, final InputStream content, final String contentType) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement("EntityProvider", "readLink");

    final String uriString = EntityProvider.readLink(contentType, entitySet, content);

    context.stopRuntimeMeasurement(timingHandle);

    final Map<String, Object> targetKeys = parseLinkUri(entitySet, uriString);
    if (targetKeys == null) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY);
    }
    return targetKeys;
  }

  private Map<String, Object> parseLinkUri(final EdmEntitySet targetEntitySet, final String uriString) throws ODataException {
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

    if (uri == null) {
      return null;
    } else if (uri.getTargetEntitySet() == null
        || uri.getTargetEntitySet() != targetEntitySet
        || !uri.getNavigationSegments().isEmpty()
        || uri.getKeyPredicates().isEmpty()) {
      throw new ODataBadRequestException(ODataBadRequestException.BODY);
    } else {
      return mapKey(uri.getKeyPredicates());
    }
  }

  private <T> void createInlinedEntities(final EdmEntitySet entitySet, final T data, final ODataEntry entryValues) throws ODataException {
    final EdmEntityType entityType = entitySet.getEntityType();
    for (final String navigationPropertyName : entityType.getNavigationPropertyNames()) {

      final EdmNavigationProperty navigationProperty = (EdmNavigationProperty) entityType.getProperty(navigationPropertyName);
      final EdmEntitySet relatedEntitySet = entitySet.getRelatedEntitySet(navigationProperty);

      final Object relatedValue = entryValues.getProperties().get(navigationPropertyName);
      if (relatedValue == null) {
        for (final String uriString : entryValues.getMetadata().getAssociationUris(navigationPropertyName)) {
          final Map<String, Object> key = parseLinkUri(relatedEntitySet, uriString);
          if (key != null) {
            dataSource.writeRelation(entitySet, data, relatedEntitySet, key);
          }
        }

      } else {
        if (relatedValue instanceof ODataFeed) {
          ODataFeed feed = (ODataFeed) relatedValue;
          final List<ODataEntry> relatedValueList = feed.getEntries();
          for (final ODataEntry relatedValues : relatedValueList) {
            Object relatedData = dataSource.newDataObject(relatedEntitySet);
            setStructuralTypeValuesFromMap(relatedData, relatedEntitySet.getEntityType(), relatedValues.getProperties(), true);
            dataSource.createData(relatedEntitySet, relatedData);
            dataSource.writeRelation(entitySet, data, relatedEntitySet, getStructuralTypeValueMap(relatedData, relatedEntitySet.getEntityType()));
            createInlinedEntities(relatedEntitySet, relatedData, relatedValues);
          }
        } else if (relatedValue instanceof ODataEntry) {
          final ODataEntry relatedValueEntry = (ODataEntry) relatedValue;
          Object relatedData = dataSource.newDataObject(relatedEntitySet);
          setStructuralTypeValuesFromMap(relatedData, relatedEntitySet.getEntityType(), relatedValueEntry.getProperties(), true);
          dataSource.createData(relatedEntitySet, relatedData);
          dataSource.writeRelation(entitySet, data, relatedEntitySet, getStructuralTypeValueMap(relatedData, relatedEntitySet.getEntityType()));
          createInlinedEntities(relatedEntitySet, relatedData, relatedValueEntry);
        } else {
          throw new ODataException("Unexpected class for a related value: " + relatedValue.getClass().getSimpleName());
        }

      }
    }
  }

  private <T> Integer applySystemQueryOptions(final EdmEntitySet entitySet, final List<T> data, final FilterExpression filter, final InlineCount inlineCount, final OrderByExpression orderBy, final String skipToken, final Integer skip, final Integer top) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "applySystemQueryOptions");

    if (filter != null) {
      // Remove all elements the filter does not apply for.
      // A for-each loop would not work with "remove", see Java documentation.
      for (Iterator<T> iterator = data.iterator(); iterator.hasNext();) {
        if (!appliesFilter(iterator.next(), filter)) {
          iterator.remove();
        }
      }
    }

    final Integer count = inlineCount == InlineCount.ALLPAGES ? data.size() : null;

    if (orderBy != null) {
      sort(data, orderBy);
    } else if (skipToken != null || skip != null || top != null) {
      sortInDefaultOrder(entitySet, data);
    }

    if (skipToken != null) {
      while (!data.isEmpty() && !getSkipToken(entitySet, data.get(0)).equals(skipToken)) {
        data.remove(0);
      }
    }

    if (skip != null) {
      if (skip >= data.size()) {
        data.clear();
      } else {
        for (int i = 0; i < skip; i++) {
          data.remove(0);
        }
      }
    }

    if (top != null) {
      while (data.size() > top) {
        data.remove(top.intValue());
      }
    }

    context.stopRuntimeMeasurement(timingHandle);

    return count;
  }

  private static <T> void sort(final List<T> data, final OrderByExpression orderBy) {
    Collections.sort(data, new Comparator<T>() {
      @Override
      public int compare(final T entity1, final T entity2) {
        try {
          int result = 0;
          for (final OrderExpression expression : orderBy.getOrders()) {
            result = evaluateExpression(entity1, expression.getExpression()).compareTo(
                evaluateExpression(entity2, expression.getExpression()));
            if (expression.getSortOrder() == SortOrder.desc) {
              result = -result;
            }
            if (result != 0) {
              break;
            }
          }
          return result;
        } catch (final ODataException e) {
          return 0;
        }
      }
    });
  }

  private static <T> void sortInDefaultOrder(final EdmEntitySet entitySet, final List<T> data) {
    Collections.sort(data, new Comparator<T>() {
      @Override
      public int compare(final T entity1, final T entity2) {
        try {
          return getSkipToken(entitySet, entity1).compareTo(getSkipToken(entitySet, entity2));
        } catch (final ODataException e) {
          return 0;
        }
      }
    });
  }

  private <T> boolean appliesFilter(final T data, final FilterExpression filter) throws ODataException {
    if (data == null) {
      return false;
    }

    if (filter == null) {
      return true;
    }

    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "appliesFilter");

    try {
      return evaluateExpression(data, filter.getExpression()).equals("true");
    } catch (final RuntimeException e) {
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
        return operand.startsWith("-") ? operand.substring(1) : "-" + operand;
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
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
          return Double.toString(Double.valueOf(left) + Double.valueOf(right));
        } else {
          return Long.toString(Long.valueOf(left) + Long.valueOf(right));
        }
      case SUB:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
          return Double.toString(Double.valueOf(left) - Double.valueOf(right));
        } else {
          return Long.toString(Long.valueOf(left) - Long.valueOf(right));
        }
      case MUL:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
          return Double.toString(Double.valueOf(left) * Double.valueOf(right));
        } else {
          return Long.toString(Long.valueOf(left) * Long.valueOf(right));
        }
      case DIV:
        final String number = Double.toString(Double.valueOf(left) / Double.valueOf(right));
        return number.endsWith(".0") ? number.replace(".0", "") : number;
      case MODULO:
        if (binaryExpression.getEdmType() == EdmSimpleTypeKind.Decimal.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Double.getEdmSimpleTypeInstance()
            || binaryExpression.getEdmType() == EdmSimpleTypeKind.Single.getEdmSimpleTypeInstance()) {
          return Double.toString(Double.valueOf(left) % Double.valueOf(right));
        } else {
          return Long.toString(Long.valueOf(left) % Long.valueOf(right));
        }
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
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
          return Boolean.toString(left.compareTo(right) < 0);
        } else {
          return Boolean.toString(Double.valueOf(left) < Double.valueOf(right));
        }
      case LE:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
          return Boolean.toString(left.compareTo(right) <= 0);
        } else {
          return Boolean.toString(Double.valueOf(left) <= Double.valueOf(right));
        }
      case GT:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
          return Boolean.toString(left.compareTo(right) > 0);
        } else {
          return Boolean.toString(Double.valueOf(left) > Double.valueOf(right));
        }
      case GE:
        if (type == EdmSimpleTypeKind.String.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTime.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.DateTimeOffset.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Guid.getEdmSimpleTypeInstance()
            || type == EdmSimpleTypeKind.Time.getEdmSimpleTypeInstance()) {
          return Boolean.toString(left.compareTo(right) >= 0);
        } else {
          return Boolean.toString(Double.valueOf(left) >= Double.valueOf(right));
        }
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
      while (currentExpression != null) {
        final PropertyExpression currentPropertyExpression =
            (PropertyExpression) (currentExpression.getKind() == ExpressionKind.MEMBER ?
                ((MemberExpression) currentExpression).getProperty() : currentExpression);
        final EdmTyped currentProperty = currentPropertyExpression.getEdmProperty();
        final EdmTypeKind kind = currentProperty.getType().getKind();
        if (kind == EdmTypeKind.SIMPLE || kind == EdmTypeKind.COMPLEX) {
          propertyPath.add(0, (EdmProperty) currentProperty);
        } else {
          throw new ODataNotImplementedException();
        }
        currentExpression = currentExpression.getKind() == ExpressionKind.MEMBER ? ((MemberExpression) currentExpression).getPath() : null;
      }
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
        final int offset = Integer.parseInt(second);
        return first.substring(offset, offset + Integer.parseInt(third));
      case SUBSTRINGOF:
        return Boolean.toString(second.contains(first));
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
    for (final EdmProperty property : propertyPath) {
      if (dataObject != null) {
        dataObject = getPropertyValue(dataObject, property);
      }
    }
    return dataObject;
  }

  private static <T> Object getPropertyValue(final T data, final EdmProperty property) throws ODataException {
    return getValue(data, getGetterMethodName(property));
  }

  private static <T> Class<?> getPropertyType(final T data, final EdmProperty property) throws ODataException {
    return getType(data, getGetterMethodName(property));
  }

  private static <T, V> void setPropertyValue(final T data, final EdmProperty property, final V value) throws ODataException {
    final String methodName = getSetterMethodName(getGetterMethodName(property));
    if (methodName != null) {
      setValue(data, methodName, value);
    }
  }

  private static String getGetterMethodName(final EdmProperty property) throws EdmException {
    final String prefix = property.isSimple() && property.getType() == EdmSimpleTypeKind.Boolean.getEdmSimpleTypeInstance() ? "is" : "get";
    final String defaultMethodName = prefix + property.getName();
    return property.getMapping() == null || property.getMapping().getInternalName() == null ?
        defaultMethodName : property.getMapping().getInternalName();
  }

  private static String getSetterMethodName(final String getterMethodName) {
    return getterMethodName.contains(".") ?
        null : getterMethodName.replaceFirst("^is", "set").replaceFirst("^get", "set");
  }

  private static <T> Map<String, Object> getSimpleTypeValueMap(final T data, final List<EdmProperty> propertyPath) throws ODataException {
    final EdmProperty property = propertyPath.get(propertyPath.size() - 1);
    Map<String, Object> valueWithMimeType = new HashMap<String, Object>();
    valueWithMimeType.put(property.getName(), getPropertyValue(data, propertyPath));
    final String mimeTypeMappingName = property.getMapping().getMimeType();
    valueWithMimeType.put(mimeTypeMappingName, getValue(data, mimeTypeMappingName));
    return valueWithMimeType;
  }

  private <T> Map<String, Object> getStructuralTypeValueMap(final T data, final EdmStructuralType type) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "getStructuralTypeValueMap");

    Map<String, Object> valueMap = new HashMap<String, Object>();

    if (type.getMapping() != null && type.getMapping().getMimeType() != null) {
      final String methodName = type.getMapping().getMimeType();
      valueMap.put(methodName, getValue(data, methodName));
    }

    for (final String propertyName : type.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
      final Object value = getPropertyValue(data, property);

      if (property.isSimple()) {
        if (property.getMapping() == null || property.getMapping().getMimeType() == null) {
          valueMap.put(propertyName, value);
        } else {
          // TODO: enable MIME type mapping outside the current subtree
          valueMap.put(propertyName, getSimpleTypeValueMap(data, Arrays.asList(property)));
        }
      } else {
        valueMap.put(propertyName, getStructuralTypeValueMap(value, (EdmStructuralType) property.getType()));
      }
    }

    context.stopRuntimeMeasurement(timingHandle);

    return valueMap;
  }

  private <T> Map<String, Object> getStructuralTypeTypeMap(final T data, final EdmStructuralType type) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "getStructuralTypeTypeMap");

    Map<String, Object> typeMap = new HashMap<String, Object>();
    for (final String propertyName : type.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
      if (property.isSimple()) {
        typeMap.put(propertyName, getPropertyType(data, property));
      } else {
        typeMap.put(propertyName, getStructuralTypeTypeMap(getPropertyValue(data, property), (EdmStructuralType) property.getType()));
      }
    }

    context.stopRuntimeMeasurement(timingHandle);

    return typeMap;
  }

  private <T> void setStructuralTypeValuesFromMap(final T data, final EdmStructuralType type, final Map<String, Object> valueMap, final boolean merge) throws ODataException {
    ODataContext context = getContext();
    final int timingHandle = context.startRuntimeMeasurement(getClass().getSimpleName(), "setStructuralTypeValuesFromMap");

    for (final String propertyName : type.getPropertyNames()) {
      final EdmProperty property = (EdmProperty) type.getProperty(propertyName);
      if (type instanceof EdmEntityType && ((EdmEntityType) type).getKeyProperties().contains(property)) {
        continue;
      }
      if (property.isSimple()) {
        final Object value = valueMap.get(propertyName);
        if (value != null || !merge) {
          setPropertyValue(data, property, value);
        }
      } else {
        @SuppressWarnings("unchecked")
        final Map<String, Object> values = (Map<String, Object>) valueMap.get(propertyName);
        if (values != null || !merge) {
          setStructuralTypeValuesFromMap(getPropertyValue(data, property), (EdmStructuralType) property.getType(), values, merge);
        }
      }
    }

    context.stopRuntimeMeasurement(timingHandle);
  }

  private static <T> Object getValue(final T data, final String methodName) throws ODataNotFoundException {
    Object dataObject = data;

    for (final String method : methodName.split("\\.", -1)) {
      if (dataObject != null) {
        try {
          dataObject = dataObject.getClass().getMethod(method).invoke(dataObject);
        } catch (SecurityException e) {
          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
        } catch (NoSuchMethodException e) {
          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
        } catch (IllegalArgumentException e) {
          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
        } catch (IllegalAccessException e) {
          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
        } catch (InvocationTargetException e) {
          throw new ODataNotFoundException(ODataHttpException.COMMON, e);
        }
      }
    }

    return dataObject;
  }

  private static <T> Class<?> getType(final T data, final String methodName) throws ODataNotFoundException {
    if (data == null) {
      throw new ODataNotFoundException(ODataHttpException.COMMON);
    }

    Class<?> type = data.getClass();
    for (final String method : methodName.split("\\.", -1)) {
      try {
        type = type.getMethod(method).getReturnType();
        if (type.isPrimitive()) {
          if (type == boolean.class) {
            type = Boolean.class;
          } else if (type == byte.class) {
            type = Byte.class;
          } else if (type == short.class) {
            type = Short.class;
          } else if (type == int.class) {
            type = Integer.class;
          } else if (type == long.class) {
            type = Long.class;
          } else if (type == float.class) {
            type = Float.class;
          } else if (type == double.class) {
            type = Double.class;
          }
        }
      } catch (final SecurityException e) {
        throw new ODataNotFoundException(ODataHttpException.COMMON, e);
      } catch (final NoSuchMethodException e) {
        throw new ODataNotFoundException(ODataHttpException.COMMON, e);
      }
    }
    return type;
  }

  private static <T, V> void setValue(final T data, final String methodName, final V value) throws ODataNotFoundException {
    try {
      boolean found = false;
      for (final Method method : Arrays.asList(data.getClass().getMethods())) {
        if (method.getName().equals(methodName)) {
          found = true;
          final Class<?> type = method.getParameterTypes()[0];
          if (value == null) {
            if (type.equals(byte.class) || type.equals(short.class) || type.equals(int.class) || type.equals(long.class) || type.equals(char.class)) {
              method.invoke(data, 0);
            } else if (type.equals(float.class) || type.equals(double.class)) {
              method.invoke(data, 0.0);
            } else if (type.equals(boolean.class)) {
              method.invoke(data, false);
            } else {
              method.invoke(data, value);
            }
          } else {
            method.invoke(data, value);
          }
          break;
        }
      }
      if (!found) {
        throw new ODataNotFoundException(null);
      }
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
