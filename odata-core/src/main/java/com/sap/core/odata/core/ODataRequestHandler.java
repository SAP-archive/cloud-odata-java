package com.sap.core.odata.core;

import java.util.List;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.batch.BatchQueryPart;
import com.sap.core.odata.api.batch.BatchChangesetPart;
import com.sap.core.odata.api.batch.ODataRequestHandlerInterface;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriType;

/**
 * @author SAP AG
 */
public class ODataRequestHandler implements ODataRequestHandlerInterface {

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
   * @throws ODataException
   */
  @Override
  public ODataResponse handle(final ODataRequest request) throws ODataException {
    ODataContextImpl context = buildODataContext(request);

    final int timingHandle = context.startRuntimeMeasurement("ODataRequestHandler", "handle");

    UriInfoImpl uriInfo = null;
    ODataResponse odataResponse;
    Exception exception = null;
    try {
      service = serviceFactory.createService(context);
      context.setService(service);
      service.getProcessor().setContext(context);

      UriParser uriParser = new UriParserImpl(service.getEntityDataModel());
      Dispatcher dispatcher = new Dispatcher(serviceFactory, service, new ContentNegotiator());

      final String serverDataServiceVersion = getServerDataServiceVersion();
      final String requestDataServiceVersion = context.getRequestHeader(ODataHttpHeaders.DATASERVICEVERSION);
      validateDataServiceVersion(serverDataServiceVersion, requestDataServiceVersion);

      final List<PathSegment> pathSegments = context.getPathInfo().getODataSegments();

      int timingHandle2 = context.startRuntimeMeasurement("UriParserImpl", "parse");
      uriInfo = (UriInfoImpl) uriParser.parse(pathSegments, request.getQueryParameters());
      context.stopRuntimeMeasurement(timingHandle2);

      timingHandle2 = context.startRuntimeMeasurement("Dispatcher", "dispatch");
      odataResponse = dispatcher.dispatch(request.getMethod(), uriInfo, request.getBody(), request.getContentType(), request.getAcceptHeaders());
      context.stopRuntimeMeasurement(timingHandle2);

      ODataHttpMethod method = request.getMethod();
      final String location = (method == ODataHttpMethod.POST && (uriInfo.getUriType() == UriType.URI1 || uriInfo.getUriType() == UriType.URI6B)) ? odataResponse.getIdLiteral() : null;
      final HttpStatusCodes s = odataResponse.getStatus() == null ? method == ODataHttpMethod.POST ? uriInfo.getUriType() == UriType.URI9 ? HttpStatusCodes.OK : uriInfo.getUriType() == UriType.URI7B ? HttpStatusCodes.NO_CONTENT : HttpStatusCodes.CREATED : method == ODataHttpMethod.PUT || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE || method == ODataHttpMethod.DELETE ? HttpStatusCodes.NO_CONTENT : HttpStatusCodes.OK : odataResponse.getStatus();

      ODataResponseBuilder extendedResponse = ODataResponse.fromResponse(odataResponse);
      if (!odataResponse.containsHeader(ODataHttpHeaders.DATASERVICEVERSION)) {
        extendedResponse = extendedResponse.header(ODataHttpHeaders.DATASERVICEVERSION, serverDataServiceVersion);
      }
      if (!odataResponse.containsHeader(HttpHeaders.LOCATION) && location != null) {
        extendedResponse = extendedResponse.header(HttpHeaders.LOCATION, location);
      }
      if (odataResponse.getStatus() != s) {
        extendedResponse = extendedResponse.status(s);
      }
      if (odataResponse.getETag() != null) {
        extendedResponse = extendedResponse.header(HttpHeaders.ETAG, odataResponse.getETag());
      }
      odataResponse = extendedResponse.build();

    } catch (final Exception e) {
      exception = e;
      ODataExceptionWrapper exceptionWrapper = new ODataExceptionWrapper(context, request.getQueryParameters(), request.getAcceptHeaders());
      odataResponse = exceptionWrapper.wrapInExceptionResponse(exception);
    }
    context.stopRuntimeMeasurement(timingHandle);

    return odataResponse;
  }

  private ODataContextImpl buildODataContext(final ODataRequest request) {
    ODataContextImpl context = new ODataContextImpl();

    context.setServiceFactory(serviceFactory);
    context.setRequest(request);
    context.setPathInfo(request.getPathInfo());
    context.setHttpMethod(request.getMethod().name());
    context.setAcceptableLanguages(request.getAcceptableLanguages());

    return context;
  }

  private String getServerDataServiceVersion() throws ODataException {
    String serverDataServiceVersion = ODataServiceVersion.V20;
    if (service.getVersion() != null) {
      serverDataServiceVersion = service.getVersion();
    }
    return serverDataServiceVersion;
  }

  private void validateDataServiceVersion(final String serverDataServiceVersion, final String requestDataServiceVersion) throws ODataException {

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

  @Override
  public ODataResponse handle(BatchPart batchPart) throws ODataException{
    if (batchPart.isChangeSet()) {
      BatchChangesetPart changeset = (BatchChangesetPart) batchPart;
      
      return service.getBatchProcessor().executeChangeSet(this, changeset);
    } else {
      BatchQueryPart batchQueryPart = (BatchQueryPart) batchPart;
     return handle(batchQueryPart.getRequest());
    }
  }
  
}
