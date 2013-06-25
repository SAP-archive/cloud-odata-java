package com.sap.core.odata.core;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataDebugCallback;
import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.processor.part.EntityLinkProcessor;
import com.sap.core.odata.api.processor.part.EntityLinksProcessor;
import com.sap.core.odata.api.processor.part.EntityMediaProcessor;
import com.sap.core.odata.api.processor.part.EntityProcessor;
import com.sap.core.odata.api.processor.part.EntitySetProcessor;
import com.sap.core.odata.api.processor.part.EntitySimplePropertyValueProcessor;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.debug.ODataDebugResponseWrapper;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriType;

/**
 * @author SAP AG
 */
public class ODataRequestHandler {

  private ODataServiceFactory serviceFactory;
  private ODataService service;

  public ODataRequestHandler(final ODataServiceFactory factory) {
    serviceFactory = factory;
  }

  /**
   * <p>Handles the {@link ODataRequest} in a way that it results in a corresponding {@link ODataResponse}.</p>
   * <p>This includes building of the {@link com.sap.core.odata.api.processor.ODataContext ODataContext},
   * delegation of URI parsing and dispatching of the request internally.</p>
   * @param request the incoming request
   * @return the corresponding result
   */
  public ODataResponse handle(final ODataRequest request) {
    ODataContextImpl context = buildODataContext(request);

    final int timingHandle = context.startRuntimeMeasurement("ODataRequestHandler", "handle");

    UriInfoImpl uriInfo = null;
    Exception exception = null;
    ODataResponse odataResponse;
    try {
      service = serviceFactory.createService(context);
      context.setService(service);
      service.getProcessor().setContext(context);

      UriParser uriParser = new UriParserImpl(service.getEntityDataModel());
      Dispatcher dispatcher = new Dispatcher(serviceFactory, service);

      final String serverDataServiceVersion = getServerDataServiceVersion();
      final String requestDataServiceVersion = context.getRequestHeader(ODataHttpHeaders.DATASERVICEVERSION);
      validateDataServiceVersion(serverDataServiceVersion, requestDataServiceVersion);

      final List<PathSegment> pathSegments = context.getPathInfo().getODataSegments();
      int timingHandle2 = context.startRuntimeMeasurement("UriParserImpl", "parse");
      uriInfo = (UriInfoImpl) uriParser.parse(pathSegments, request.getQueryParameters());
      context.stopRuntimeMeasurement(timingHandle2);

      final ODataHttpMethod method = request.getMethod();
      final UriType uriType = uriInfo.getUriType();
      validateMethodAndUri(method, uriInfo);

      if (method == ODataHttpMethod.POST || method == ODataHttpMethod.PUT
          || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE) {
        checkRequestContentType(uriInfo, request.getContentType());
      }

      final String acceptContentType = new ContentNegotiator().doContentNegotiation(uriInfo, request.getAcceptHeaders(), getSupportedContentTypes(uriInfo));

      timingHandle2 = context.startRuntimeMeasurement("Dispatcher", "dispatch");
      odataResponse = dispatcher.dispatch(method, uriInfo, request.getBody(), request.getContentType(), acceptContentType);
      context.stopRuntimeMeasurement(timingHandle2);

      final String location = (method == ODataHttpMethod.POST && (uriType == UriType.URI1 || uriType == UriType.URI6B)) ? odataResponse.getIdLiteral() : null;
      final HttpStatusCodes s = odataResponse.getStatus() == null ? method == ODataHttpMethod.POST ? uriType == UriType.URI9 ? HttpStatusCodes.OK : uriType == UriType.URI7B ? HttpStatusCodes.NO_CONTENT : HttpStatusCodes.CREATED : method == ODataHttpMethod.PUT || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE || method == ODataHttpMethod.DELETE ? HttpStatusCodes.NO_CONTENT : HttpStatusCodes.OK : odataResponse.getStatus();

      ODataResponseBuilder extendedResponse = ODataResponse.fromResponse(odataResponse);
      if (!odataResponse.containsHeader(ODataHttpHeaders.DATASERVICEVERSION)) {
        extendedResponse = extendedResponse.header(ODataHttpHeaders.DATASERVICEVERSION, serverDataServiceVersion);
      }
      extendedResponse = extendedResponse.idLiteral(location).status(s);
      odataResponse = extendedResponse.build();

    } catch (final Exception e) {
      exception = e;
      odataResponse = new ODataExceptionWrapper(context, request.getQueryParameters(), request.getAcceptHeaders())
          .wrapInExceptionResponse(e);
    }
    context.stopRuntimeMeasurement(timingHandle);

    final String debugValue = getDebugValue(context, request.getQueryParameters());
    return debugValue == null ?
        odataResponse : new ODataDebugResponseWrapper(context, odataResponse, uriInfo, exception, debugValue).wrapResponse();
  }

  private ODataContextImpl buildODataContext(final ODataRequest request) {
    ODataContextImpl context = new ODataContextImpl();

    context.setServiceFactory(serviceFactory);
    context.setRequest(request);
    context.setPathInfo(request.getPathInfo());
    context.setHttpMethod(request.getMethod().name());
    context.setAcceptableLanguages(request.getAcceptableLanguages());
    context.setDebugMode(checkDebugMode(request.getQueryParameters()));

    return context;
  }

  private String getServerDataServiceVersion() throws ODataException {
    return service.getVersion() == null ? ODataServiceVersion.V20 : service.getVersion();
  }

  private static void validateDataServiceVersion(final String serverDataServiceVersion, final String requestDataServiceVersion) throws ODataException {
    if (requestDataServiceVersion != null) {
      try {
        final boolean isValid = ODataServiceVersion.validateDataServiceVersion(requestDataServiceVersion);
        if (!isValid || ODataServiceVersion.isBiggerThan(requestDataServiceVersion, serverDataServiceVersion)) {
          throw new ODataBadRequestException(ODataBadRequestException.VERSIONERROR.addContent(requestDataServiceVersion.toString()));
        }
      } catch (final IllegalArgumentException e) {
        throw new ODataBadRequestException(ODataBadRequestException.PARSEVERSIONERROR.addContent(requestDataServiceVersion), e);
      }
    }
  }

  private static void validateMethodAndUri(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    validateUriMethod(method, uriInfo);
    checkFunctionImport(method, uriInfo);
    if (method != ODataHttpMethod.GET) {
      checkNotGetSystemQueryOptions(method, uriInfo);
      checkNumberOfNavigationSegments(uriInfo);
      checkProperty(method, uriInfo);
    }
  }

  private static void validateUriMethod(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI0:
    case URI8:
      if (method != ODataHttpMethod.GET) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI1:
    case URI6B:
    case URI7B:
      if (method != ODataHttpMethod.GET && method != ODataHttpMethod.POST) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI2:
    case URI6A:
    case URI7A:
      if (method != ODataHttpMethod.GET
          && method != ODataHttpMethod.PUT
          && method != ODataHttpMethod.DELETE
          && method != ODataHttpMethod.PATCH && method != ODataHttpMethod.MERGE) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI3:
      if (method != ODataHttpMethod.GET
          && method != ODataHttpMethod.PUT
          && method != ODataHttpMethod.PATCH && method != ODataHttpMethod.MERGE) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI4:
    case URI5:
      if (method != ODataHttpMethod.GET
          && method != ODataHttpMethod.PUT
          && method != ODataHttpMethod.DELETE
          && method != ODataHttpMethod.PATCH && method != ODataHttpMethod.MERGE) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      } else if (method == ODataHttpMethod.DELETE && !uriInfo.isValue()) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI9:
      if (method != ODataHttpMethod.POST) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI10:
    case URI11:
    case URI12:
    case URI13:
    case URI14:
      break;

    case URI15:
    case URI16:
    case URI50A:
    case URI50B:
      if (method != ODataHttpMethod.GET) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI17:
      if (method != ODataHttpMethod.GET
          && method != ODataHttpMethod.PUT
          && method != ODataHttpMethod.DELETE) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    default:
      throw new ODataRuntimeException("Unknown or not implemented URI type: " + uriInfo.getUriType());
    }
  }

  private static void checkFunctionImport(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    if (uriInfo.getFunctionImport() != null
        && uriInfo.getFunctionImport().getHttpMethod() != null
        && !uriInfo.getFunctionImport().getHttpMethod().equals(method.toString())) {
      throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
    }
  }

  private static void checkNotGetSystemQueryOptions(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI1:
    case URI6B:
      if (uriInfo.getFormat() != null
          || uriInfo.getFilter() != null
          || uriInfo.getInlineCount() != null
          || uriInfo.getOrderBy() != null
          || uriInfo.getSkipToken() != null
          || uriInfo.getSkip() != null
          || uriInfo.getTop() != null
          || !uriInfo.getExpand().isEmpty()
          || !uriInfo.getSelect().isEmpty()) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI2:
      if (uriInfo.getFormat() != null
          || !uriInfo.getExpand().isEmpty()
          || !uriInfo.getSelect().isEmpty()) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      if (method == ODataHttpMethod.DELETE) {
        if (uriInfo.getFilter() != null) {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      }
      break;

    case URI3:
      if (uriInfo.getFormat() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI4:
    case URI5:
      if (method == ODataHttpMethod.PUT || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE) {
        if (!uriInfo.isValue() && uriInfo.getFormat() != null) {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      }
      break;

    case URI7A:
      if (uriInfo.getFormat() != null || uriInfo.getFilter() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI7B:
      if (uriInfo.getFormat() != null
          || uriInfo.getFilter() != null
          || uriInfo.getInlineCount() != null
          || uriInfo.getOrderBy() != null
          || uriInfo.getSkipToken() != null
          || uriInfo.getSkip() != null
          || uriInfo.getTop() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    case URI17:
      if (uriInfo.getFormat() != null || uriInfo.getFilter() != null) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      break;

    default:
      break;
    }
  }

  private static void checkNumberOfNavigationSegments(final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI1:
    case URI6B:
    case URI7A:
    case URI7B:
      if (uriInfo.getNavigationSegments().size() > 1) {
        throw new ODataBadRequestException(ODataBadRequestException.NOTSUPPORTED);
      }
      break;

    case URI3:
    case URI4:
    case URI5:
    case URI17:
      if (!uriInfo.getNavigationSegments().isEmpty()) {
        throw new ODataBadRequestException(ODataBadRequestException.NOTSUPPORTED);
      }
      break;

    default:
      break;
    }
  }

  private static void checkProperty(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    if (uriInfo.getUriType() == UriType.URI4 || uriInfo.getUriType() == UriType.URI5) {
      if (isPropertyKey(uriInfo)) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      if (method == ODataHttpMethod.DELETE
          && !isPropertyNullable(getProperty(uriInfo))) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
    }
  }

  private static EdmProperty getProperty(final UriInfo uriInfo) {
    final List<EdmProperty> propertyPath = uriInfo.getPropertyPath();
    return propertyPath == null || propertyPath.isEmpty() ? null : propertyPath.get(propertyPath.size() - 1);
  }

  private static boolean isPropertyKey(final UriInfo uriInfo) throws EdmException {
    final EdmEntityType entityType = uriInfo.getTargetEntitySet().getEntityType();
    return entityType.getKeyProperties().contains(getProperty(uriInfo));
  }

  private static boolean isPropertyNullable(final EdmProperty property) throws EdmException {
    return property.getFacets() == null || property.getFacets().isNullable();
  }

  private void checkRequestContentType(final UriInfoImpl uriInfo, final String contentTypeString) throws ODataException {
    final ContentType contentType = ContentType.parse(contentTypeString);
    if (contentType == null || contentType.hasWildcard()) {
      throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
    }

    Class<? extends ODataProcessor> processorFeature = Dispatcher.mapUriTypeToProcessorFeature(uriInfo);
    // Adjust processor feature.
    if (processorFeature == EntitySetProcessor.class) {
      processorFeature = uriInfo.getTargetEntitySet().getEntityType().hasStream() ?
          EntityMediaProcessor.class : // A media resource can have any type.
          EntityProcessor.class; // The request must contain a single entity!
    } else if (processorFeature == EntityLinksProcessor.class)
    {
      processorFeature = EntityLinkProcessor.class; // The request must contain a single link!
    }

    final List<ContentType> supportedContentTypes = processorFeature == EntitySimplePropertyValueProcessor.class ?
        getSupportedContentTypes(getProperty(uriInfo)) :
        getSupportedContentTypes(processorFeature);

    if (!isValidRequestContentType(contentType, supportedContentTypes)) {
      throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
    }
  }

  private static boolean isValidRequestContentType(final ContentType contentType, final List<ContentType> allowedContentTypes) {
    final ContentType requested = contentType.receiveWithCharsetParameter(ContentNegotiator.DEFAULT_CHARSET);
    if (requested.getODataFormat() == ODataFormat.CUSTOM || requested.getODataFormat() == ODataFormat.MIME) {
      return requested.hasCompatible(allowedContentTypes);
    }
    return requested.hasMatch(allowedContentTypes);
  }

  private static List<ContentType> getSupportedContentTypes(final EdmProperty property) throws EdmException {
    return property.getType() == EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance() ?
        Arrays.asList(property.getMimeType() == null ? ContentType.WILDCARD : ContentType.create(property.getMimeType())) :
        Arrays.asList(ContentType.TEXT_PLAIN, ContentType.TEXT_PLAIN_CS_UTF_8);
  }

  private List<String> getSupportedContentTypes(final UriInfoImpl uriInfo) throws ODataException {
    final Class<? extends ODataProcessor> processorFeature = Dispatcher.mapUriTypeToProcessorFeature(uriInfo);
    return service.getSupportedContentTypes(processorFeature);
  }

  private List<ContentType> getSupportedContentTypes(final Class<? extends ODataProcessor> processorFeature) throws ODataException {
    List<String> contentTypeStrings = service.getSupportedContentTypes(processorFeature);
    return ContentType.create(contentTypeStrings);
  }

  private static String getDebugValue(final ODataContext context, final Map<String, String> queryParameters) {
    return context.isInDebugMode() ? getQueryDebugValue(queryParameters) : null;
  }

  private boolean checkDebugMode(final Map<String, String> queryParameters) {
    if (getQueryDebugValue(queryParameters) == null) {
      return false;
    } else {
      final ODataDebugCallback callback = serviceFactory.getCallback(ODataDebugCallback.class);
      return callback != null && callback.isDebugEnabled();
    }
  }

  private static String getQueryDebugValue(final Map<String, String> queryParameters) {
    final String debugValue = queryParameters.get(ODataDebugResponseWrapper.ODATA_DEBUG_QUERY_PARAMETER);
    return ODataDebugResponseWrapper.ODATA_DEBUG_JSON.equals(debugValue) ?
        debugValue : null;
  }
}
