package com.sap.core.odata.core.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.core.dispatcher.Dispatcher;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.service.ODataSingleProcessorService;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriParserResultImpl;

public final class ODataLocatorImpl {

  private ODataService service;

  private Dispatcher dispatcher;

  private UriParserImpl uriParser;

  private ODataContextImpl context;

  private Map<String, String> queryParameters;

  @GET
  public Response handleGet() throws ODataException {
    try {
      List<String> pathSegments = new ArrayList<String>(this.context.getODataPathSegment());
      UriParserResultImpl uriParserResult = (UriParserResultImpl) this.uriParser.parse(pathSegments, this.queryParameters);

      ODataResponse odataResponse = dispatcher.dispatch(ODataHttpMethod.GET, uriParserResult);
      Response response = this.convertResponse(odataResponse);

      return response;
    } catch (ODataException e) {
      throw new RuntimeException(e);
    }
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      ) throws ODataException {

    Response response;

    /* tunneling */
    if (xmethod == null) {
      response = Response.ok().entity("POST: status 200 ok").build();
    } else if ("MERGE".equals(xmethod)) {
      response = this.handleMerge();
    } else if ("PATCH".equals(xmethod)) {
      response = this.handlePatch();
    } else if ("DELETE".equals(xmethod)) {
      response = this.handleDelete();
    } else {
      response = Response.status(405).build(); // method not allowed!
    }

    return response;
  }

  @PUT
  public Response handlePut() throws ODataException {
    return Response.ok().entity("PUT: status 200 ok").build();
  }

  @PATCH
  public Response handlePatch() throws ODataException {
    return Response.ok().entity("PATCH: status 200 ok").build();
  }

  @MERGE
  public Response handleMerge() throws ODataException {
    return Response.ok().entity("MERGE: status 200 ok").build();
  }

  @DELETE
  public Response handleDelete() throws ODataException {
    return Response.ok().entity("DELETE: status 200 ok").build();
  }

  public void setContext(ODataContextImpl context) {
    this.context = context;
  }

  public void initializeService(InitParameter param) throws ODataException {
    this.context = new ODataContextImpl();

    this.splittPath(param);

    this.queryParameters = this.convertToSinglevaluedMap(param.getUriInfo().getQueryParameters());

    EdmProvider provider = param.getServiceFactory().createProvider();
    Edm edm = new EdmImplProv(provider);

    ODataProcessor processor = param.getServiceFactory().createProcessor();
    this.service = new ODataSingleProcessorService(processor, edm);
    this.context.setService(this.service);

    processor.setContext(this.context);
    
    this.uriParser = new UriParserImpl(service.getEntityDataModel());
    this.dispatcher = new Dispatcher(this.service);
  }

  private void splittPath(InitParameter param) throws ODataException {
    List<PathSegment> precedingPathSegements;
    List<PathSegment> odataPathSegements;

    if (param.getPathSplit() == 0) {
      precedingPathSegements = Collections.emptyList();
      odataPathSegements = param.getPathSegments();
    } else {
      if (param.getPathSegments().size() < param.getPathSplit()) {
        throw new ODataBadRequestException(ODataBadRequestException.URLTOSHORT);
      }
      
      precedingPathSegements = param.getPathSegments().subList(0, param.getPathSplit());
      int pathSegmentCount = param.getPathSegments().size();
      odataPathSegements = param.getPathSegments().subList(param.getPathSplit(), pathSegmentCount);
    }

    this.context.setODataPathSegment(this.getPathSegmentsAsStrings(odataPathSegements));
    this.context.setPrecedingPathSegment(this.getPathSegmentsAsStrings(precedingPathSegements));
  }

  public List<String> getPathSegmentsAsStrings(List<PathSegment> pathSegments) {
    ArrayList<String> pathSegmentsAsString = new ArrayList<String>();

    for (PathSegment pathSegment : pathSegments) {
      pathSegmentsAsString.add(pathSegment.getPath());
    }
    return pathSegmentsAsString;
  }

  private Map<String, String> convertToSinglevaluedMap(MultivaluedMap<String, String> multi) {
    Map<String, String> single = new HashMap<String, String>();

    for (String key : multi.keySet()) {
      String value = multi.getFirst(key);
      single.put(key, value);
    }

    return single;
  }

  private Response convertResponse(final ODataResponse odataResponse) {
    ResponseBuilder responseBuilder = Response.noContent();

    responseBuilder = responseBuilder.status(odataResponse.getStatus().getStatusCode());
    responseBuilder = responseBuilder.entity(odataResponse.getEntity());

    for (String name : odataResponse.getHeaderNames())
      responseBuilder = responseBuilder.header(name, odataResponse.getHeader(name));

    String eTag = odataResponse.getETag();
    if (eTag != null) {
      responseBuilder.header(HttpHeaders.ETAG, eTag);
    }

    return responseBuilder.build();
  }

  public class InitParameter {

    private List<PathSegment> pathSegments;
    private HttpHeaders httpHeaders;
    private UriInfo uriInfo;
    private Request request;
    private int pathSplit;
    private ODataServiceFactory serviceFactory;

    public ODataServiceFactory getServiceFactory() {
      return serviceFactory;
    }

    public void setServiceFactory(ODataServiceFactory serviceFactory) {
      this.serviceFactory = serviceFactory;
    }

    public List<PathSegment> getPathSegments() {
      return pathSegments;
    }

    public void setPathSegments(List<PathSegment> pathSegments) {
      this.pathSegments = pathSegments;
    }

    public HttpHeaders getHttpHeaders() {
      return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
      this.httpHeaders = httpHeaders;
    }

    public UriInfo getUriInfo() {
      return uriInfo;
    }

    public void setUriInfo(UriInfo uriInfo) {
      this.uriInfo = uriInfo;
    }

    public Request getRequest() {
      return request;
    }

    public void setRequest(Request request) {
      this.request = request;
    }

    public int getPathSplit() {
      return pathSplit;
    }

    public void setPathSplit(int pathSplit) {
      this.pathSplit = pathSplit;
    }
  }

}
