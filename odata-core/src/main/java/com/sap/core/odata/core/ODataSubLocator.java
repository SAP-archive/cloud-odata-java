package com.sap.core.odata.core;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataPathSegment;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.processor.aspect.ProcessorAspect;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.service.ODataServiceFactory;
import com.sap.core.odata.core.enums.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriParserResultImpl;

public final class ODataSubLocator implements ODataLocator {

  private ODataService service;

  private Dispatcher dispatcher;

  private UriParserImpl uriParser;

  private ODataContextImpl context = new ODataContextImpl();

  private Map<String, String> queryParameters;

  private List<ContentType> acceptHeaderContentTypes;

  @GET
  public Response handleGet() throws ODataException {
    List<ODataPathSegment> pathSegments = this.context.getUriInfo().getODataPathSegmentList(); //
    UriParserResultImpl uriParserResult = (UriParserResultImpl) this.uriParser.parse(pathSegments, this.queryParameters);

    ContentType contentType = doContentNegotiation(uriParserResult);

    ODataResponse odataResponse = dispatcher.dispatch(ODataHttpMethod.GET, uriParserResult, contentType);
    Response response = this.convertResponse(odataResponse);

    return response;
  }

  private ContentType doContentNegotiation(UriParserResultImpl uriParserResult) throws ODataException {
    ProcessorAspect processorAspect = dispatcher.mapUriTypeToProcessorAspect(uriParserResult);
    List<ContentType> supportedContentTypes = service.getSupportedContentTypes(processorAspect);
    List<ContentType> acceptedContentTypes = getAcceptedContentTypes(uriParserResult, acceptHeaderContentTypes);
    ContentType contentType = contentNegotiation(acceptedContentTypes, supportedContentTypes);
    return contentType;
  }

  ContentType contentNegotiation(List<ContentType> contentTypes, List<ContentType> supportedContentTypes) throws ODataException {
    Set<ContentType> setSupported = new HashSet<ContentType>(supportedContentTypes);

    if (contentTypes.isEmpty()) {
      if (!setSupported.isEmpty()) {
        return supportedContentTypes.get(0);
      } 
    } else {
      for (ContentType ct : contentTypes) {
        ContentType match = ct.match(supportedContentTypes);
        if(match != null) {
          return match;
        }
      }
    }

    throw new ODataNotAcceptableException(ODataNotAcceptableException.COMMON.addContent(contentTypes.toString()));
  }


  private List<ContentType> getAcceptedContentTypes(UriParserResultImpl uriParserResult, List<ContentType> contentTypes) {
    List<ContentType> result;
    if (uriParserResult.getContentType() != null) {
      result = new ArrayList<ContentType>();
      result.add(uriParserResult.getContentType());
    } else {
      result = contentTypes;
    }

    return result;
  }

  @POST
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
      response = Response.status(Status.METHOD_NOT_ALLOWED).build();
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
    final List<ODataPathSegment> pathSegments = context.getUriInfo().getODataPathSegmentList();
    final UriParserResultImpl uriParserResult = (UriParserResultImpl) uriParser.parse(pathSegments, queryParameters);

    final ODataResponse odataResponse = dispatcher.dispatch(ODataHttpMethod.DELETE, uriParserResult, null);
    return convertResponse(odataResponse);
  }

  @OPTIONS
  public Response handleOptions() throws ODataException {
    throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.COMMON);
  }

  @HEAD
  public Response handleHead() throws ODataException {
    throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.COMMON);
  }

  public void initialize(InitParameter param) throws ODataException {
    this.context.setUriInfo(this.buildODataUriInfo(param));

    this.queryParameters = this.convertToSinglevaluedMap(param.getUriInfo().getQueryParameters());

    acceptHeaderContentTypes = convertMediaTypes(param.httpHeaders.getAcceptableMediaTypes());

    this.service = param.getServiceFactory().createService(this.context);
    this.context.setService(this.service);
    this.service.getProcessor().setContext(this.context);

    this.uriParser = new UriParserImpl(service.getEntityDataModel());
    this.dispatcher = new Dispatcher(this.service);
  }

  private List<ContentType> convertMediaTypes(List<javax.ws.rs.core.MediaType> acceptableMediaTypes) {
    List<ContentType> mediaTypes = new ArrayList<ContentType>();

    for (javax.ws.rs.core.MediaType x : acceptableMediaTypes) {
      ContentType mt = ContentType.create(x.getType(), x.getSubtype(), x.getParameters());
      mediaTypes.add(mt);
    }

    return mediaTypes;
  }

  private ODataUriInfo buildODataUriInfo(InitParameter param) throws ODataException {
    ODataUriInfoImpl odataUriInfo = new ODataUriInfoImpl();

    this.splitPath(odataUriInfo, param);

    URI uri = buildBaseUri(param.getUriInfo(), odataUriInfo.getPrecedingPathSegmentList());
    odataUriInfo.setBaseUri(uri);

    this.context.setUriInfo(odataUriInfo);

    return odataUriInfo;
  }

  private void splitPath(ODataUriInfoImpl odataUriInfo, InitParameter param) throws ODataException {
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

    // post condition: we do not allow matrix parameter in OData path segments
    for (PathSegment ps : odataPathSegements) {
      if (ps.getMatrixParameters() != null && !ps.getMatrixParameters().isEmpty()) {
        throw new ODataNotFoundException(ODataNotFoundException.MATRIX.addContent(ps.getMatrixParameters().keySet(), ps.getPath()));
      }
    }

    odataUriInfo.setODataPathSegment(this.convertPathSegmentList(odataPathSegements));
    odataUriInfo.setPrecedingPathSegment(this.convertPathSegmentList(precedingPathSegements));
  }

  private URI buildBaseUri(UriInfo uriInfo, List<ODataPathSegment> precedingPathSegements) throws ODataException {
    try {
      UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
      for (ODataPathSegment ps : precedingPathSegements) {
        uriBuilder = uriBuilder.path(ps.getPath());
        for (String key : ps.getMatrixParameters().keySet()) {
          Object[] v = ps.getMatrixParameters().get(key).toArray();
          uriBuilder = uriBuilder.matrixParam(key, v);
        }
      }

      String uriString = uriBuilder.build().toString();
      if (!uriString.endsWith("/")) {
        uriString = uriString + "/";
      }

      return new URI(uriString);
    } catch (URISyntaxException e) {
      throw new ODataException(e);
    }
  }

  public List<ODataPathSegment> convertPathSegmentList(List<PathSegment> pathSegments) {
    ArrayList<ODataPathSegment> converted = new ArrayList<ODataPathSegment>();

    for (PathSegment pathSegment : pathSegments) {
      ODataPathSegment segment = new ODataPathSegmentImpl(pathSegment.getPath(), pathSegment.getMatrixParameters());
      converted.add(segment);
    }
    return converted;
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
