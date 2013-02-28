package com.sap.core.odata.core.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.exception.ODataMethodNotAllowedException;
import com.sap.core.odata.api.exception.ODataNotAcceptableException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.core.Dispatcher;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ContentType.ODataFormat;
import com.sap.core.odata.core.commons.Decoder;
import com.sap.core.odata.core.commons.ODataHttpMethod;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriType;

/**
 * @author SAP AG
 */
public final class ODataSubLocator implements ODataLocator {

  private static final String DEFAULT_CHARSET = "utf-8";

  private ODataService service;

  private Dispatcher dispatcher;

  private UriParserImpl uriParser;

  private final ODataContextImpl context = new ODataContextImpl();

  private Map<String, String> queryParameters;

  private List<ContentType> acceptHeaderContentTypes;

  private InputStream requestContent;
  private ContentType requestContentTypeHeader;

  @GET
  public Response handleGet() throws ODataException {
    return handleHttpMethod(ODataHttpMethod.GET);
  }

  @PUT
  public Response handlePut() throws ODataException {
    return handleHttpMethod(ODataHttpMethod.PUT);
  }

  @PATCH
  public Response handlePatch() throws ODataException {
    return handleHttpMethod(ODataHttpMethod.PATCH);
  }

  @MERGE
  public Response handleMerge() throws ODataException {
    return handleHttpMethod(ODataHttpMethod.MERGE);
  }

  @DELETE
  public Response handleDelete() throws ODataException {
    return handleHttpMethod(ODataHttpMethod.DELETE);
  }

  @POST
  public Response handlePost(@HeaderParam("X-HTTP-Method") final String xHttpMethod) throws ODataException {
    Response response;

    if (xHttpMethod == null) {
      response = handleHttpMethod(ODataHttpMethod.POST);
    } else {
      /* tunneling */
      if ("MERGE".equals(xHttpMethod)) {
        response = handleMerge();
      } else if ("PATCH".equals(xHttpMethod)) {
        response = handlePatch();
      } else if ("DELETE".equals(xHttpMethod)) {
        response = handleDelete();
      } else if ("PUT".equals(xHttpMethod)) {
        response = handlePut();
      } else {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.TUNNELING);
      }
    }
    return response;
  }

  @OPTIONS
  public Response handleOptions() throws ODataException {
    throw new ODataMethodNotAllowedException(ODataMessageException.COMMON);
  }

  @HEAD
  public Response handleHead() throws ODataException {
    throw new ODataMethodNotAllowedException(ODataMessageException.COMMON);
  }

  private Response handleHttpMethod(final ODataHttpMethod method) throws ODataException {
    final String serverDataServiceVersion = getServerDataServiceVersion();
    validateDataServiceVersion(serverDataServiceVersion);
    final List<PathSegment> pathSegments = context.getPathInfo().getODataSegments();
    final UriInfoImpl uriInfo = (UriInfoImpl) uriParser.parse(pathSegments, queryParameters);

    checkFunctionImport(method, uriInfo);
    if (method != ODataHttpMethod.GET) {
      checkNotGetSystemQueryOptions(method, uriInfo);
      checkNumberOfNavigationSegments(uriInfo);
      checkProperty(method, uriInfo);

      if (method == ODataHttpMethod.POST || method == ODataHttpMethod.PUT
          || method == ODataHttpMethod.PATCH || method == ODataHttpMethod.MERGE) {
        checkRequestContentType(uriInfo, requestContentTypeHeader);
      }
    }

    final String acceptContentType = doContentNegotiation(uriInfo);
    final String requestContentType = (requestContentTypeHeader == null ? null : requestContentTypeHeader.toContentTypeString());

    final ODataResponse odataResponse = dispatcher.dispatch(method, uriInfo, requestContent, requestContentType, acceptContentType);

    final String location = (method == ODataHttpMethod.POST && (uriInfo.getUriType() == UriType.URI1 || uriInfo.getUriType() == UriType.URI6B)) ? odataResponse.getIdLiteral() : null;
    final Response response = convertResponse(odataResponse, serverDataServiceVersion, location);

    return response;
  }

  private void checkFunctionImport(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    if (uriInfo.getFunctionImport() != null
        && uriInfo.getFunctionImport().getHttpMethod() != null
        && !uriInfo.getFunctionImport().getHttpMethod().equals(method.toString())) {
      throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
    }
  }

  private void checkNotGetSystemQueryOptions(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
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

  private void checkNumberOfNavigationSegments(final UriInfoImpl uriInfo) throws ODataException {
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

  private void checkProperty(final ODataHttpMethod method, final UriInfoImpl uriInfo) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI4:
    case URI5:
      if (isPropertyKey(uriInfo)) {
        throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
      }
      if (method == ODataHttpMethod.DELETE) {
        if (!isPropertyNullable(getProperty(uriInfo))) {
          throw new ODataMethodNotAllowedException(ODataMethodNotAllowedException.DISPATCH);
        }
      }
      break;

    default:
      break;
    }
  }

  private EdmProperty getProperty(final UriInfo uriInfo) {
    return uriInfo.getPropertyPath().get(uriInfo.getPropertyPath().size() - 1);
  }

  private boolean isPropertyKey(final UriInfo uriInfo) throws EdmException {
    final EdmEntityType entityType = uriInfo.getTargetEntitySet().getEntityType();
    return entityType.getKeyProperties().contains(getProperty(uriInfo));
  }

  private boolean isPropertyNullable(final EdmProperty property) throws EdmException {
    return property.getFacets() == null || property.getFacets().isNullable();
  }

  private void checkRequestContentType(final UriInfoImpl uriInfo, final ContentType contentType) throws ODataException {
    switch (uriInfo.getUriType()) {
    case URI1:
    case URI6B:
    case URI2:
      final Class<? extends ODataProcessor> processorFeature = dispatcher.mapUriTypeToProcessorFeature(uriInfo);
      final List<ContentType> supportedContentTypes = getSupportedContentTypes(processorFeature);
      if (UriType.URI1.equals(uriInfo.getUriType())) {
        supportedContentTypes.add(ContentType.APPLICATION_ATOM_XML_ENTRY_CS_UTF_8);
      }
      if (uriInfo.isValue()) {
        supportedContentTypes.add(ContentType.TEXT_PLAIN);
      }
      EdmEntityType entityType = uriInfo.getTargetEntitySet().getEntityType();
      if (entityType.hasStream()) {
        if (entityType.getMapping() != null && entityType.getMapping().getMimeType() != null) {
          String mimeType = entityType.getMapping().getMimeType();
          supportedContentTypes.add(ContentType.create(mimeType));
        }
        supportedContentTypes.addAll(Arrays.asList(ContentType.TEXT_PLAIN, ContentType.TEXT_PLAIN_CS_UTF_8, ContentType.APPLICATION_OCTET_STREAM));
      }
      if (!isValidRequestContentType(contentType, supportedContentTypes)) {
        throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
      }

    case URI4:
    case URI5:
      if (uriInfo.isValue()) {
        if (!isValidRequestContentTypeForProperty(getProperty(uriInfo), contentType)) {
          throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(contentType));
        }
      }

    default:
      break;
    }
  }

  private boolean isValidRequestContentType(final ContentType contentType, final List<ContentType> allowedContentTypes) {
    if (contentType == null) {
      return false;
    }

    final ContentType requested = ensureCharsetIsSet(contentType);
    return requested.hasMatch(allowedContentTypes);
  }

  private boolean isValidRequestContentTypeForProperty(final EdmProperty property, final ContentType contentType) throws EdmException {
    final ContentType requested = ensureCharsetIsSet(contentType);
    final String mimeType = property.getMimeType();
    if (mimeType != null) {
      return requested.equals(ContentType.create(mimeType));
    } else {
      return requested.hasMatch(Arrays.asList(ContentType.TEXT_PLAIN, ContentType.TEXT_PLAIN_CS_UTF_8, ContentType.APPLICATION_OCTET_STREAM));
    }
  }

  private String doContentNegotiation(final UriInfoImpl uriInfo) throws ODataException {
    ContentType contentType;
    if (uriInfo.getFormat() == null) {
      contentType = doContentNegotiationForAcceptHeader(uriInfo);
    } else {
      contentType = doContentNegotiationForFormat(uriInfo);
    }

    if (contentType.getODataFormat() == ODataFormat.CUSTOM) {
      return contentType.getType();
    }
    return contentType.toContentTypeString();
  }

  private ContentType doContentNegotiationForFormat(final UriInfoImpl uriInfo) throws ODataException {
    ContentType formatContentType = mapFormat(uriInfo);
    formatContentType = ensureCharsetIsSet(formatContentType);

    final Class<? extends ODataProcessor> processorFeature = dispatcher.mapUriTypeToProcessorFeature(uriInfo);
    final List<ContentType> supportedContentTypes = getSupportedContentTypes(processorFeature);
    for (final ContentType contentType : supportedContentTypes) {
      if (contentType.equals(formatContentType)) {
        return formatContentType;
      }
    }

    throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(uriInfo.getFormat()));
  }

  private ContentType ensureCharsetIsSet(final ContentType contentType) {
    if (isContentTypeODataTextRelated(contentType)) {
      if (!contentType.getParameters().containsKey(ContentType.PARAMETER_CHARSET)) {
        return ContentType.create(contentType, ContentType.PARAMETER_CHARSET, DEFAULT_CHARSET);
      }
    }
    return contentType;
  }

  private boolean isContentTypeODataTextRelated(final ContentType contentType) {
    return (contentType != null) && (contentType.equals(ContentType.TEXT_PLAIN) || (contentType.getODataFormat() == ODataFormat.XML) || (contentType.getODataFormat() == ODataFormat.ATOM) || (contentType.getODataFormat() == ODataFormat.JSON));
  }

  private ContentType mapFormat(final UriInfoImpl uriInfo) {
    final String format = uriInfo.getFormat();
    if ("xml".equals(format)) {
      return ContentType.APPLICATION_XML;
    } else if ("atom".equals(format)) {
      if (uriInfo.getUriType() == UriType.URI0) {
        // special handling for serviceDocument uris (UriType.URI0)
        return ContentType.APPLICATION_ATOM_SVC;
      }
      return ContentType.APPLICATION_ATOM_XML;
    } else if ("json".equals(format)) {
      return ContentType.APPLICATION_JSON;
    }

    return ContentType.create(format);
  }

  private ContentType doContentNegotiationForAcceptHeader(final UriInfoImpl uriInfo) throws ODataException {
    final Class<? extends ODataProcessor> processorFeature = dispatcher.mapUriTypeToProcessorFeature(uriInfo);
    final List<ContentType> supportedContentTypes = getSupportedContentTypes(processorFeature);
    return contentNegotiation(acceptHeaderContentTypes, supportedContentTypes);
  }

  private List<ContentType> getSupportedContentTypes(final Class<? extends ODataProcessor> processorFeature) throws ODataException {
    final List<ContentType> resultContentTypes = new ArrayList<ContentType>();
    for (final String contentType : service.getSupportedContentTypes(processorFeature)) {
      resultContentTypes.add(ContentType.create(contentType));
    }

    return resultContentTypes;
  }

  ContentType contentNegotiation(final List<ContentType> acceptedContentTypes, final List<ContentType> supportedContentTypes) throws ODataException {
    final Set<ContentType> setSupported = new HashSet<ContentType>(supportedContentTypes);

    if (acceptedContentTypes.isEmpty()) {
      if (!setSupported.isEmpty()) {
        return supportedContentTypes.get(0);
      }
    } else {
      for (ContentType contentType : acceptedContentTypes) {
        contentType = ensureCharsetIsSet(contentType);
        final ContentType match = contentType.match(supportedContentTypes);
        if (match != null) {
          return match;
        }
      }
    }

    throw new ODataNotAcceptableException(ODataNotAcceptableException.NOT_SUPPORTED_CONTENT_TYPE.addContent(acceptedContentTypes.toString()));
  }

  public void initialize(final InitParameter param) throws ODataException {
    fillRequestHeader(param.getHttpHeaders());
    context.setUriInfo(buildODataUriInfo(param));

    queryParameters = convertToSinglevaluedMap(param.getUriInfo().getQueryParameters());

    acceptHeaderContentTypes = convertMediaTypes(param.getHttpHeaders().getAcceptableMediaTypes());
    requestContent = contentAsStream(extractRequestContent(param));
    requestContentTypeHeader = extractRequestContentType(param);
    service = param.getServiceFactory().createService(context);
    context.setService(service);
    service.getProcessor().setContext(context);
    context.setAcceptableLanguages(param.httpHeaders.getAcceptableLanguages());

    uriParser = new UriParserImpl(service.getEntityDataModel());
    dispatcher = new Dispatcher(service);
  }

  String getServerDataServiceVersion() throws ODataException {
    String serverDataServiceVersion = ODataServiceVersion.V20;
    if (service.getVersion() != null) {
      serverDataServiceVersion = service.getVersion();
    }
    return serverDataServiceVersion;
  }

  private void validateDataServiceVersion(final String serverDataServiceVersion) throws ODataException {
    final String requestDataServiceVersion = context.getHttpRequestHeader(ODataHttpHeaders.DATASERVICEVERSION);
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

  private ContentType extractRequestContentType(final InitParameter param) throws ODataBadRequestException {
    final MediaType requestMediaType = param.getHttpHeaders().getMediaType();
    if (requestMediaType == null) {
      return null;
    } else {
      try {
        return ContentType.create(requestMediaType.toString());
      } catch (IllegalArgumentException e) {
        throw new ODataBadRequestException(ODataBadRequestException.INVALID_HEADER.addContent("Content-Type").addContent(requestMediaType.toString()), e);
      }
    }
  }

  /**
   * Extracts the request content from the servlet as input stream.
   * @param param initialization parameters
   * @return the request content as input stream
   * @throws ODataException
   */
  private ServletInputStream extractRequestContent(final InitParameter param) throws ODataException {
    try {
      return param.getServletRequest().getInputStream();
    } catch (final IOException e) {
      throw new ODataException("Error getting request content as ServletInputStream.", e);
    }
  }

  private static <T> InputStream contentAsStream(final T content) throws ODataException {
    if (content == null) {
      throw new ODataBadRequestException(ODataBadRequestException.COMMON);
    }

    InputStream inputStream;
    if (content instanceof InputStream) {
      inputStream = (InputStream) content;
    } else if (content instanceof String) {
      try {
        inputStream = new ByteArrayInputStream(((String) content).getBytes("UTF-8"));
      } catch (final UnsupportedEncodingException e) {
        throw new ODataBadRequestException(ODataBadRequestException.COMMON, e);
      }
    } else {
      throw new ODataBadRequestException(ODataBadRequestException.COMMON);
    }
    return inputStream;
  }

  private List<ContentType> convertMediaTypes(final List<MediaType> acceptableMediaTypes) {
    final List<ContentType> mediaTypes = new ArrayList<ContentType>();

    for (final MediaType x : acceptableMediaTypes) {
      mediaTypes.add(ContentType.create(x.getType(), x.getSubtype(), x.getParameters()));
    }

    return mediaTypes;
  }

  private void fillRequestHeader(final javax.ws.rs.core.HttpHeaders httpHeaders) {
    final MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();

    for (final String key : headers.keySet()) {
      final String value = httpHeaders.getHeaderString(key);
      context.setHttpRequestHeader(key, value);
    }
  }

  private PathInfo buildODataUriInfo(final InitParameter param) throws ODataException {
    final PathInfoImpl pathInfo = new PathInfoImpl();

    splitPath(pathInfo, param);

    final URI uri = buildBaseUri(param.getUriInfo(), pathInfo.getPrecedingSegments());
    pathInfo.setServiceRoot(uri);

    context.setUriInfo(pathInfo);

    return pathInfo;
  }

  private void splitPath(final PathInfoImpl pathInfo, final InitParameter param) throws ODataException {
    List<javax.ws.rs.core.PathSegment> precedingPathSegments;
    List<javax.ws.rs.core.PathSegment> pathSegments;

    if (param.getPathSplit() == 0) {
      precedingPathSegments = Collections.emptyList();
      pathSegments = param.getPathSegments();
    } else {
      if (param.getPathSegments().size() < param.getPathSplit()) {
        throw new ODataBadRequestException(ODataBadRequestException.URLTOOSHORT);
      }

      precedingPathSegments = param.getPathSegments().subList(0, param.getPathSplit());
      final int pathSegmentCount = param.getPathSegments().size();
      pathSegments = param.getPathSegments().subList(param.getPathSplit(), pathSegmentCount);
    }

    // post condition: we do not allow matrix parameters in OData path segments
    for (final javax.ws.rs.core.PathSegment ps : pathSegments) {
      if ((ps.getMatrixParameters() != null) && !ps.getMatrixParameters().isEmpty()) {
        throw new ODataNotFoundException(ODataNotFoundException.MATRIX.addContent(ps.getMatrixParameters().keySet(), ps.getPath()));
      }
    }

    pathInfo.setODataPathSegment(convertPathSegmentList(pathSegments));
    pathInfo.setPrecedingPathSegment(convertPathSegmentList(precedingPathSegments));
  }

  private URI buildBaseUri(final javax.ws.rs.core.UriInfo uriInfo, final List<PathSegment> precedingPathSegments) throws ODataException {
    try {
      UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
      for (final PathSegment ps : precedingPathSegments) {
        uriBuilder = uriBuilder.path(ps.getPath());
        for (final String key : ps.getMatrixParameters().keySet()) {
          final Object[] v = ps.getMatrixParameters().get(key).toArray();
          uriBuilder = uriBuilder.matrixParam(key, v);
        }
      }

      String uriString = uriBuilder.build().toString();
      if (!uriString.endsWith("/")) {
        uriString = uriString + "/";
      }

      return new URI(uriString);
    } catch (final URISyntaxException e) {
      throw new ODataException(e);
    }
  }

  public List<PathSegment> convertPathSegmentList(final List<javax.ws.rs.core.PathSegment> pathSegments) {
    final ArrayList<PathSegment> converted = new ArrayList<PathSegment>();

    for (final javax.ws.rs.core.PathSegment pathSegment : pathSegments) {
      final PathSegment segment = new ODataPathSegmentImpl(Decoder.decode(pathSegment.getPath()), pathSegment.getMatrixParameters());
      converted.add(segment);
    }
    return converted;
  }

  private Map<String, String> convertToSinglevaluedMap(final MultivaluedMap<String, String> multi) {
    final Map<String, String> single = new HashMap<String, String>();

    for (final String key : multi.keySet()) {
      final String value = multi.getFirst(key);
      single.put(key, value);
    }

    return single;
  }

  private Response convertResponse(final ODataResponse odataResponse, final String version, final String location) {
    ResponseBuilder responseBuilder = Response.noContent().status(odataResponse.getStatus().getStatusCode()).entity(odataResponse.getEntity());

    for (final String name : odataResponse.getHeaderNames()) {
      responseBuilder = responseBuilder.header(name, odataResponse.getHeader(name));
    }

    if (!odataResponse.containsHeader(ODataHttpHeaders.DATASERVICEVERSION)) {
      responseBuilder = responseBuilder.header(ODataHttpHeaders.DATASERVICEVERSION, version);
    }

    if (!odataResponse.containsHeader(HttpHeaders.LOCATION) && location != null)
      responseBuilder = responseBuilder.header(HttpHeaders.LOCATION, location);

    final String eTag = odataResponse.getETag();
    if (eTag != null) {
      responseBuilder.header(HttpHeaders.ETAG, eTag);
    }

    return responseBuilder.build();
  }

  public class InitParameter {

    private List<javax.ws.rs.core.PathSegment> pathSegments;
    private javax.ws.rs.core.HttpHeaders httpHeaders;
    private javax.ws.rs.core.UriInfo uriInfo;
    private Request request;
    private int pathSplit;
    private ODataServiceFactory serviceFactory;
    private HttpServletRequest servletRequest;

    public ODataServiceFactory getServiceFactory() {
      return serviceFactory;
    }

    public void setServiceFactory(final ODataServiceFactory serviceFactory) {
      this.serviceFactory = serviceFactory;
    }

    public List<javax.ws.rs.core.PathSegment> getPathSegments() {
      return pathSegments;
    }

    public void setPathSegments(final List<javax.ws.rs.core.PathSegment> pathSegments) {
      this.pathSegments = pathSegments;
    }

    public javax.ws.rs.core.HttpHeaders getHttpHeaders() {
      return httpHeaders;
    }

    public void setHttpHeaders(final javax.ws.rs.core.HttpHeaders httpHeaders) {
      this.httpHeaders = httpHeaders;
    }

    public javax.ws.rs.core.UriInfo getUriInfo() {
      return uriInfo;
    }

    public void setUriInfo(final javax.ws.rs.core.UriInfo uriInfo) {
      this.uriInfo = uriInfo;
    }

    public Request getRequest() {
      return request;
    }

    public void setRequest(final Request request) {
      this.request = request;
    }

    public int getPathSplit() {
      return pathSplit;
    }

    public void setPathSplit(final int pathSplit) {
      this.pathSplit = pathSplit;
    }

    public void setServletRequest(final HttpServletRequest servletRequest) {
      this.servletRequest = servletRequest;
    }

    public HttpServletRequest getServletRequest() {
      return servletRequest;
    }
  }

}
