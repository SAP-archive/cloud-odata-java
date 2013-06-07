package com.sap.core.odata.core.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotImplementedException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.ODataExceptionWrapper;
import com.sap.core.odata.core.ODataRequestHandler;
import com.sap.core.odata.core.ODataRequestImpl;

/**
 * @author SAP AG
 */
public final class ODataSubLocator implements ODataLocator {

  private ODataRequestImpl request;

  private ODataRequestHandler requestHandler;

  private ODataServiceFactory serviceFactory;

  @GET
  public Response handleGet() throws ODataException {
    return handle(ODataHttpMethod.GET);
  }

  @PUT
  public Response handlePut() throws ODataException {
    return handle(ODataHttpMethod.PUT);
  }

  @PATCH
  public Response handlePatch() throws ODataException {
    return handle(ODataHttpMethod.PATCH);
  }

  @MERGE
  public Response handleMerge() throws ODataException {
    return handle(ODataHttpMethod.MERGE);
  }

  @DELETE
  public Response handleDelete() throws ODataException {
    return handle(ODataHttpMethod.DELETE);
  }

  @POST
  public Response handlePost(@HeaderParam("X-HTTP-Method") final String xHttpMethod) throws ODataException {
    Response response;

    if (xHttpMethod == null) {
      response = handle(ODataHttpMethod.POST);
    } else {
      /* tunneling */
      if ("MERGE".equals(xHttpMethod)) {
        response = handle(ODataHttpMethod.MERGE);
      } else if ("PATCH".equals(xHttpMethod)) {
        response = handle(ODataHttpMethod.PATCH);
      } else if (HttpMethod.DELETE.equals(xHttpMethod)) {
        response = handle(ODataHttpMethod.DELETE);
      } else if (HttpMethod.PUT.equals(xHttpMethod)) {
        response = handle(ODataHttpMethod.PUT);
      } else if (HttpMethod.GET.equals(xHttpMethod)) {
        response = handle(ODataHttpMethod.GET);
      } else if (HttpMethod.POST.equals(xHttpMethod)) {
        response = handle(ODataHttpMethod.POST);
      } else if (HttpMethod.HEAD.equals(xHttpMethod)) {
        response = handleHead();
      } else if (HttpMethod.OPTIONS.equals(xHttpMethod)) {
        response = handleOptions();
      } else {
        response = returnNotImplementedResponse(ODataNotImplementedException.TUNNELING);
      }
    }
    return response;
  }

  private Response returnNotImplementedResponse(final MessageReference messageReference) {
    // RFC 2616, 5.1.1: "An origin server SHOULD return the status code [...]
    // 501 (Not Implemented) if the method is unrecognized [...] by the origin server."
    ODataContextImpl context = new ODataContextImpl();
    context.setRequest(request);
    context.setAcceptableLanguages(request.getAcceptableLanguages());
    context.setPathInfo(request.getPathInfo());
    context.setServiceFactory(serviceFactory);
    ODataExceptionWrapper exceptionWrapper = new ODataExceptionWrapper(context, request.getQueryParameters(), request.getAcceptHeaders());
    ODataResponse response = exceptionWrapper.wrapInExceptionResponse(new ODataNotImplementedException(messageReference));
    return RestUtil.convertResponse(response);
  }

  @OPTIONS
  public Response handleOptions() throws ODataException {
    // RFC 2616, 5.1.1: "An origin server SHOULD return the status code [...]
    // 501 (Not Implemented) if the method is unrecognized or not implemented
    // by the origin server."
    return returnNotImplementedResponse(ODataNotImplementedException.COMMON);
  }

  @HEAD
  public Response handleHead() throws ODataException {
    // RFC 2616, 5.1.1: "An origin server SHOULD return the status code [...]
    // 501 (Not Implemented) if the method is unrecognized or not implemented
    // by the origin server."
    return returnNotImplementedResponse(ODataNotImplementedException.COMMON);
  }

  private Response handle(final ODataHttpMethod method) throws ODataException {

    request.setMethod(method); // TODO skl refactor after JAX-RS elimination

    final ODataResponse odataResponse = requestHandler.handle(request);
    final Response response = RestUtil.convertResponse(odataResponse);

    return response;
  }

  public void initialize(final SubLocatorParameter param) throws ODataException {
    serviceFactory = param.getServiceFactory();
    requestHandler = new ODataRequestHandler(serviceFactory);

    request = new ODataRequestImpl();

    //Request headers are set twice to support previous versions till getHeaders() is removed.
    request.setRequestHeaders(param.getHttpHeaders().getRequestHeaders());
    request.setHeaders(RestUtil.extractRequestHeaders(param.getHttpHeaders()));
    
    request.setPathInfo(RestUtil.buildODataPathInfo(param));
    request.setBody(RestUtil.contentAsStream(RestUtil.extractRequestContent(param)));

    request.setQueryParameters(RestUtil.convertToSinglevaluedMap(param.getUriInfo().getQueryParameters()));
    request.setAcceptHeaders(RestUtil.extractAcceptHeaders(param));
    request.setContentType(RestUtil.extractRequestContentType(param));
    request.setAcceptableLanguages(param.getHttpHeaders().getAcceptableLanguages());
  }
}
