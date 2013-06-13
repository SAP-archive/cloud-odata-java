package com.sap.core.odata.core.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.exception.ODataBadRequestException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataNotFoundException;
import com.sap.core.odata.api.exception.ODataUnsupportedMediaTypeException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.Decoder;

/**
 * @author SAP AG
 */
public class RestUtil {
  public static Response convertResponse(final ODataResponse odataResponse) {
    ResponseBuilder responseBuilder = Response.noContent().status(odataResponse.getStatus().getStatusCode()).entity(odataResponse.getEntity());

    for (final String name : odataResponse.getHeaderNames()) {
      responseBuilder = responseBuilder.header(name, odataResponse.getHeader(name));
    }

    return responseBuilder.build();
  }

  public static ContentType extractRequestContentType(final SubLocatorParameter param) throws ODataUnsupportedMediaTypeException, ODataBadRequestException {
    final MediaType requestMediaType = param.getHttpHeaders().getMediaType();
    if (requestMediaType == null) {
      // RFC 2616, 7.2.1:
      // "Any HTTP/1.1 message containing an entity-body SHOULD include a
      // Content-Type header field defining the media type of that body. [...]
      // If the media type remains unknown, the recipient SHOULD treat it
      // as type "application/octet-stream"."
      return ContentType.APPLICATION_OCTET_STREAM;
    } else if (requestMediaType == MediaType.WILDCARD_TYPE
        || requestMediaType == MediaType.TEXT_PLAIN_TYPE
        || requestMediaType == MediaType.APPLICATION_XML_TYPE) {
      // The JAX-RS implementation of media-type parsing decided to
      // return one of the known constants for the special case of
      // an invalid content type that has no subtype;
      // at least CXF 2.7 is known to do that.
      // The "==" comparison above is optimized for this case.
      throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(param.getHttpHeaders().getRequestHeader(HttpHeaders.CONTENT_TYPE).get(0)));
    } else {
      try {
        final String contentType = param.getHttpHeaders().getHeaderString(HttpHeaders.CONTENT_TYPE);
        if (ContentType.isParseable(contentType)) {
          return ContentType.create(contentType);
        }
        throw new ODataBadRequestException(ODataBadRequestException.INVALID_HEADER.addContent(HttpHeaders.CONTENT_TYPE, contentType));
      } catch (IllegalArgumentException e) {
        throw new ODataUnsupportedMediaTypeException(ODataUnsupportedMediaTypeException.NOT_SUPPORTED.addContent(requestMediaType.toString()), e);
      }
    }
  }

  /**
   * Extracts the request content from the servlet as input stream.
   * @param param initialization parameters
   * @return the request content as input stream
   * @throws ODataException
   */
  public static ServletInputStream extractRequestContent(final SubLocatorParameter param) throws ODataException {
    try {
      return param.getServletRequest().getInputStream();
    } catch (final IOException e) {
      throw new ODataException("Error getting request content as ServletInputStream.", e);
    }
  }

  public static <T> InputStream contentAsStream(final T content) throws ODataException {
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

  public static List<String> extractAcceptHeaders(final SubLocatorParameter param) throws ODataBadRequestException {
    // first validate all accept header content types are 'parseable' and valif from our point of view
    List<String> acceptHeaders = param.getHttpHeaders().getRequestHeader(HttpHeaders.ACCEPT);

    for (String acceptHeader : acceptHeaders) {
      String[] contentTypes = acceptHeader.split(",");
      for (String contentType : contentTypes) {
        if (!ContentType.isParseable(contentType.trim())) {
          throw new ODataBadRequestException(ODataBadRequestException.INVALID_HEADER.addContent(HttpHeaders.ACCEPT, acceptHeader));
        }
      }
    }

    // then get sorted list of acceptable media type from jax-rs
    final List<String> mediaTypes = new ArrayList<String>();
    final List<MediaType> acceptableMediaTypes = param.getHttpHeaders().getAcceptableMediaTypes();
    for (final MediaType mediaType : acceptableMediaTypes) {
      mediaTypes.add(mediaType.toString());
    }

    return mediaTypes;
  }

  public static Map<String, String> extractRequestHeaders(final javax.ws.rs.core.HttpHeaders httpHeaders) {
    final MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
    Map<String, String> headerMap = new HashMap<String, String>();

    for (final String key : headers.keySet()) {
      List<String> header = httpHeaders.getRequestHeader(key);
      if (header != null && !header.isEmpty()) {
        /*
         * consider first header value only
         * avoid using jax-rs 2.0 (getHeaderString())
         */
        String value = header.get(0);
        if (value != null && !"".equals(value)) {
          headerMap.put(key, value);
        }
      }

    }
    return headerMap;
  }

  public static PathInfoImpl buildODataPathInfo(final SubLocatorParameter param) throws ODataException {
    final UriInfo uriInfo = param.getUriInfo();
    PathInfoImpl pathInfo = splitPath(param);
    pathInfo.setServiceRoot(buildBaseUri(param.getServletRequest(), uriInfo, pathInfo.getPrecedingSegments()));
    pathInfo.setRequestUri(uriInfo.getRequestUri());

    return pathInfo;
  }

  private static PathInfoImpl splitPath(final SubLocatorParameter param) throws ODataException {
    PathInfoImpl pathInfo = new PathInfoImpl();

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

    // Percent-decode only the preceding path segments.
    // The OData path segments are decoded during URI parsing.
    pathInfo.setPrecedingPathSegment(convertPathSegmentList(precedingPathSegments));

    List<PathSegment> odataSegments = new ArrayList<PathSegment>();
    for (final javax.ws.rs.core.PathSegment segment : pathSegments) {
      if (segment.getMatrixParameters() == null || segment.getMatrixParameters().isEmpty()) {
        odataSegments.add(new ODataPathSegmentImpl(segment.getPath(), null));
      } else {
        // post condition: we do not allow matrix parameters in OData path segments
        throw new ODataNotFoundException(ODataNotFoundException.MATRIX.addContent(segment.getMatrixParameters().keySet(), segment.getPath()));
      }
    }
    pathInfo.setODataPathSegment(odataSegments);

    return pathInfo;
  }

  private static URI buildBaseUri(final HttpServletRequest request, final javax.ws.rs.core.UriInfo uriInfo, final List<PathSegment> precedingPathSegments) throws ODataException {
    try {
      UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
      for (final PathSegment ps : precedingPathSegments) {
        uriBuilder = uriBuilder.path(ps.getPath());
        for (final String key : ps.getMatrixParameters().keySet()) {
          final Object[] v = ps.getMatrixParameters().get(key).toArray();
          uriBuilder = uriBuilder.matrixParam(key, v);
        }
      }

      /*
       * workaround because of host name is cached by uriInfo
       */
      uriBuilder.host(request.getServerName());

      String uriString = uriBuilder.build().toString();
      if (!uriString.endsWith("/")) {
        uriString = uriString + "/";
      }

      return new URI(uriString);
    } catch (final URISyntaxException e) {
      throw new ODataException(e);
    }
  }

  private static List<PathSegment> convertPathSegmentList(final List<javax.ws.rs.core.PathSegment> pathSegments) {
    ArrayList<PathSegment> converted = new ArrayList<PathSegment>();
    for (final javax.ws.rs.core.PathSegment pathSegment : pathSegments) {
      final PathSegment segment = new ODataPathSegmentImpl(Decoder.decode(pathSegment.getPath()), pathSegment.getMatrixParameters());
      converted.add(segment);
    }
    return converted;
  }

  public static Map<String, String> convertToSinglevaluedMap(final MultivaluedMap<String, String> multi) {
    final Map<String, String> single = new HashMap<String, String>();

    for (final String key : multi.keySet()) {
      final String value = multi.getFirst(key);
      single.put(key, value);
    }

    return single;
  }

}
