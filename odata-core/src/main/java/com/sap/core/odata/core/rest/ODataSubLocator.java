package com.sap.core.odata.core.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.feature.ProcessorFeature;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.core.Dispatcher;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.ODataLocator;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.ODataUriInfoImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;

/**
 * @author SAP AG
 */
public final class ODataSubLocator implements ODataLocator {

  private ODataService service;

  private Dispatcher dispatcher;

  private UriParserImpl uriParser;

  private ODataContextImpl context = new ODataContextImpl();

  private Map<String, String> queryParameters;

  private List<ContentType> acceptHeaderContentTypes;

  @GET
  public Response handleGet() throws ODataException {
    List<PathSegment> pathSegments = this.context.getUriInfo().getODataSegments();
    UriInfoImpl uriParserResult = (UriInfoImpl) this.uriParser.parse(pathSegments, this.queryParameters);

    ContentType contentType = doContentNegotiation(uriParserResult);

    ODataResponse odataResponse = dispatcher.dispatch(ODataHttpMethod.GET, uriParserResult, contentType.toContentTypeString());
    Response response = this.convertResponse(odataResponse);

    return response;
  }

  private ContentType doContentNegotiation(UriInfoImpl uriParserResult) throws ODataException {
    ProcessorFeature processorFeature = dispatcher.mapUriTypeToProcessorFeature(uriParserResult);
    List<ContentType> supportedContentTypes = getSupportedContentTypes(processorFeature);
    List<ContentType> acceptedContentTypes = getAcceptedContentTypes(uriParserResult, acceptHeaderContentTypes);
    ContentType contentType = contentNegotiation(acceptedContentTypes, supportedContentTypes);
    return contentType;
  }

  private List<ContentType> getSupportedContentTypes(ProcessorFeature processorFeature) throws ODataException {
    List<ContentType> resultContentTypes = new ArrayList<ContentType>();
    for (String contentType : service.getSupportedContentTypes(processorFeature))
      resultContentTypes.add(ContentType.create(contentType));

    return resultContentTypes;
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
        if (match != null)
          return match;
      }
    }

    throw new ODataNotAcceptableException(ODataNotAcceptableException.COMMON.addContent(contentTypes.toString()));
  }

  private List<ContentType> getAcceptedContentTypes(UriInfoImpl uriParserResult, List<ContentType> contentTypes) {
    if (uriParserResult.getContentType() == null)
      return contentTypes;
    else
     return Arrays.asList(ContentType.create(uriParserResult.getContentType()));
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
    final List<PathSegment> pathSegments = context.getUriInfo().getODataSegments();
    final UriInfoImpl uriParserResult = (UriInfoImpl) uriParser.parse(pathSegments, queryParameters);

    final ODataResponse odataResponse = dispatcher.dispatch(ODataHttpMethod.DELETE, uriParserResult, uriParserResult.getContentType());
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

  private PathInfo buildODataUriInfo(InitParameter param) throws ODataException {
    ODataUriInfoImpl odataUriInfo = new ODataUriInfoImpl();

    this.splitPath(odataUriInfo, param);

    URI uri = buildBaseUri(param.getUriInfo(), odataUriInfo.getPrecedingSegments());
    odataUriInfo.setBaseUri(uri);

    this.context.setUriInfo(odataUriInfo);

    return odataUriInfo;
  }

  private void splitPath(ODataUriInfoImpl odataUriInfo, InitParameter param) throws ODataException {
    List<javax.ws.rs.core.PathSegment> precedingPathSegments;
    List<javax.ws.rs.core.PathSegment> pathSegments;

    if (param.getPathSplit() == 0) {
      precedingPathSegments = Collections.emptyList();
      pathSegments = param.getPathSegments();
    } else {
      if (param.getPathSegments().size() < param.getPathSplit()) {
        throw new ODataBadRequestException(ODataBadRequestException.URLTOSHORT);
      }

      precedingPathSegments = param.getPathSegments().subList(0, param.getPathSplit());
      int pathSegmentCount = param.getPathSegments().size();
      pathSegments = param.getPathSegments().subList(param.getPathSplit(), pathSegmentCount);
    }

    // post condition: we do not allow matrix parameter in OData path segments
    for (javax.ws.rs.core.PathSegment ps : pathSegments) {
      if (ps.getMatrixParameters() != null && !ps.getMatrixParameters().isEmpty()) {
        throw new ODataNotFoundException(ODataNotFoundException.MATRIX.addContent(ps.getMatrixParameters().keySet(), ps.getPath()));
      }
    }

    odataUriInfo.setODataPathSegment(this.convertPathSegmentList(pathSegments));
    odataUriInfo.setPrecedingPathSegment(this.convertPathSegmentList(precedingPathSegments));
  }

  private URI buildBaseUri(javax.ws.rs.core.UriInfo uriInfo, List<PathSegment> precedingPathSegments) throws ODataException {
    try {
      UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
      for (PathSegment ps : precedingPathSegments) {
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

  public List<PathSegment> convertPathSegmentList(List<javax.ws.rs.core.PathSegment> pathSegments) {
    ArrayList<PathSegment> converted = new ArrayList<PathSegment>();

    for (javax.ws.rs.core.PathSegment pathSegment : pathSegments) {
      PathSegment segment = new ODataPathSegmentImpl(pathSegment.getPath(), pathSegment.getMatrixParameters());
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
    ResponseBuilder responseBuilder = Response.noContent()
        .status(odataResponse.getStatus().getStatusCode())
        .entity(odataResponse.getEntity());

    for (final String name : odataResponse.getHeaderNames())
      responseBuilder = responseBuilder.header(name, odataResponse.getHeader(name));

    String eTag = odataResponse.getETag();
    if (eTag != null)
      responseBuilder.header(HttpHeaders.ETAG, eTag);

    return responseBuilder.build();
  }

  public class InitParameter {

    private List<javax.ws.rs.core.PathSegment> pathSegments;
    private HttpHeaders httpHeaders;
    private javax.ws.rs.core.UriInfo uriInfo;
    private Request request;
    private int pathSplit;
    private ODataServiceFactory serviceFactory;

    public ODataServiceFactory getServiceFactory() {
      return serviceFactory;
    }

    public void setServiceFactory(ODataServiceFactory serviceFactory) {
      this.serviceFactory = serviceFactory;
    }

    public List<javax.ws.rs.core.PathSegment> getPathSegments() {
      return pathSegments;
    }

    public void setPathSegments(List<javax.ws.rs.core.PathSegment> pathSegments) {
      this.pathSegments = pathSegments;
    }

    public HttpHeaders getHttpHeaders() {
      return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
      this.httpHeaders = httpHeaders;
    }

    public javax.ws.rs.core.UriInfo getUriInfo() {
      return uriInfo;
    }

    public void setUriInfo(javax.ws.rs.core.UriInfo uriInfo) {
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
