package com.sap.core.odata.core.batch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.ep.EntityProviderBatchProperties;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.ODataRequestImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.ContentType;

public class BatchRequestParser {
  private static final String REG_EX_OPTIONAL_WHITESPACE = "\\s?";
  private static final String REG_EX_ZERO_OR_MORE_WHITESPACES = "\\s*";
  private static final String ANY_CHARACTERS = ".*";

  private static final Pattern REG_EX_HEADER = Pattern.compile("([a-zA-Z\\-]+):" + REG_EX_OPTIONAL_WHITESPACE + "(.*)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_VERSION = Pattern.compile("(?:HTTP/[0-9]\\.[0-9])?");
  private static final Pattern REG_EX_ANY_BOUNDARY_STRING = Pattern.compile("--" + ANY_CHARACTERS);
  private static final Pattern REG_EX_REQUEST_LINE = Pattern.compile("(GET|POST|PUT|DELETE|MERGE)\\s(.*)\\s?" + REG_EX_VERSION + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_BOUNDARY_PARAMETER = Pattern.compile(REG_EX_OPTIONAL_WHITESPACE + "boundary=(\".*\"|.*)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_CONTENT_TYPE = Pattern.compile(REG_EX_OPTIONAL_WHITESPACE + BatchConstants.MULTIPART_MIXED);
  private static final Pattern REG_EX_QUERY_PARAMETER = Pattern.compile("((?:\\$[a-z]+)|(?:[^\\$][^=]))=([^=]+)");

  private static final String REG_EX_BOUNDARY = "([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+\\s\\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\""; // See RFC 2046
  private String baseUri;
  private PathInfo batchRequestPathInfo;
  private String contentTypeMime;
  private String boundary;
  private String currentContentId;
  private static Set<String> HTTP_CHANGESET_METHODS;
  private static Set<String> HTTP_BATCH_METHODS;
  static {
    HTTP_CHANGESET_METHODS = new HashSet<String>();
    HTTP_CHANGESET_METHODS.add("POST");
    HTTP_CHANGESET_METHODS.add("PUT");
    HTTP_CHANGESET_METHODS.add("DELETE");
    HTTP_CHANGESET_METHODS.add("MERGE");

    HTTP_BATCH_METHODS = new HashSet<String>();
    HTTP_BATCH_METHODS.add("GET");
  }

  public BatchRequestParser(final String contentType, final EntityProviderBatchProperties properties) {
    contentTypeMime = contentType;
    batchRequestPathInfo = properties.getPathInfo();
  }

  public List<BatchPart> parse(final InputStream in) throws EntityProviderException {
    Scanner scanner = new Scanner(in).useDelimiter("\n");
    baseUri = getBaseUri();
    List<BatchPart> requestList;
    try {
      requestList = parseBatchRequest(scanner);
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      scanner.close();
      try {
        in.close();
      } catch (IOException e) {
        throw new EntityProviderException(EntityProviderException.COMMON, e);
      }
    }
    return requestList;
  }

  private List<BatchPart> parseBatchRequest(final Scanner scanner) throws EntityProviderException {
    List<BatchPart> requests = new LinkedList<BatchPart>();
    if (contentTypeMime != null) {
      boundary = getBoundary(contentTypeMime);
      parsePreamble(scanner);
      Pattern closeDelimiter = Pattern.compile("--" + boundary + "--" + REG_EX_ZERO_OR_MORE_WHITESPACES);
      while (scanner.hasNext() && !scanner.hasNext(closeDelimiter)) {
        requests.add(parseMultipart(scanner, boundary, false));
        parseNewLine(scanner);
      }
      if (scanner.hasNext(closeDelimiter)) {
        scanner.next(closeDelimiter);
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("The close delimiter is expected"));
      }
    } else {
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
    return requests;

  }

  //The method parses additional information prior to the first boundary delimiter line
  private void parsePreamble(final Scanner scanner) {
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      scanner.next();
    }
  }

  private BatchPart parseMultipart(final Scanner scanner, final String boundary, final boolean isChangeSet) throws EntityProviderException {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    BatchPart multipart = null;
    List<ODataRequest> requests = new ArrayList<ODataRequest>();
    if (scanner.hasNext("--" + boundary + REG_EX_ZERO_OR_MORE_WHITESPACES)) {
      scanner.next();
      mimeHeaders = parseHeaders(scanner);

      String contentType = mimeHeaders.get(BatchConstants.HTTP_CONTENT_TYPE);
      if (contentType == null) {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("No Content-Type field for MIME-header is present"));
      }
      if (isChangeSet) {
        if (BatchConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          currentContentId = mimeHeaders.get(BatchConstants.HTTP_CONTENT_ID);
          parseNewLine(scanner);// mandatory

          requests.add(parseRequest(scanner, isChangeSet));
          multipart = new BatchPartImpl(false, requests);
        } else {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid Content-Type field for MIME-header"));
        }
      } else {
        if (mimeHeaders.containsKey(BatchConstants.HTTP_CONTENT_ID)) {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("A Content-ID header can be included just for an Insert request within a ChangeSet"));
        }
        if (BatchConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          parseNewLine(scanner);// mandatory
          requests.add(parseRequest(scanner, isChangeSet));
          multipart = new BatchPartImpl(false, requests);
        } else if (contentType.matches(REG_EX_OPTIONAL_WHITESPACE + BatchConstants.MULTIPART_MIXED + ANY_CHARACTERS)) {
          String changeSetBoundary = getBoundary(contentType);
          List<ODataRequest> changeSetRequests = new LinkedList<ODataRequest>();
          parseNewLine(scanner);// mandatory
          Pattern changeSetCloseDelimiter = Pattern.compile("--" + changeSetBoundary + "--" + REG_EX_ZERO_OR_MORE_WHITESPACES);
          while (!scanner.hasNext(changeSetCloseDelimiter)) {
            BatchPart part = parseMultipart(scanner, changeSetBoundary, true);
            if (part.getRequests().size() == 1) {
              changeSetRequests.add(part.getRequests().get(0));
            }
          }
          scanner.next(changeSetCloseDelimiter);
          multipart = new BatchPartImpl(true, changeSetRequests);
        } else {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid Content-Type: " + contentType));
        }
      }
    } else if (scanner.hasNext(boundary + REG_EX_ZERO_OR_MORE_WHITESPACES)) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("The boundary delimiter must begin with two hyphen(\"-\") characters"));
    } else if (scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("The boundary string doesn't match the boundary from Content-Type header field " + boundary));
    } else {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("The boundary delimiter is expected"));
    }
    return multipart;

  }

  @SuppressWarnings("deprecation")
  private ODataRequest parseRequest(final Scanner scanner, final boolean isChangeSet) throws EntityProviderException {
    ODataRequestImpl request = new ODataRequestImpl();
    if (scanner.hasNext(REG_EX_REQUEST_LINE)) {
      scanner.next(REG_EX_REQUEST_LINE);
      String method = null;
      String uri = null;
      MatchResult result = scanner.match();
      if (result.groupCount() == 2) {
        method = result.group(1);
        uri = result.group(2).trim();
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON);
      }
      request.setPathInfo(parseRequestUri(uri));
      request.setQueryParameters(parseQueryParameters(uri));
      if (isChangeSet) {
        if (!HTTP_CHANGESET_METHODS.contains(method)) {
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid method.  A ChangeSet cannot contain retrieve requests"));
        }
      } else if (!HTTP_BATCH_METHODS.contains(method)) {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid method.  A Batch Request cannot contain insert, update or delete requests"));
      }
      request.setMethod(ODataHttpMethod.valueOf(method));
      Map<String, List<String>> headers = parseRequestHeaders(scanner);
      if (currentContentId != null) {
        List<String> headerList = new ArrayList<String>();
        headerList.add(currentContentId);
        headers.put(BatchConstants.HTTP_CONTENT_ID, headerList);
      }
      request.setRequestHeaders(headers);

      request.setHeaders(mapHeaderListToString(headers));
      if (request.getRequestHeaderValue(BatchConstants.HTTP_CONTENT_TYPE) != null) {
        request.setContentType(ContentType.create(request.getRequestHeaderValue(BatchConstants.HTTP_CONTENT_TYPE)));
      }
      if (request.getRequestHeaderValue(BatchConstants.ACCEPT) != null) {
        request.setAcceptHeaders(parseAcceptHeaders(request.getRequestHeaderValue(BatchConstants.ACCEPT)));
      } else {
        request.setAcceptHeaders(new ArrayList<String>());
      }
      if (request.getRequestHeaderValue(BatchConstants.ACCEPT_LANGUAGE) != null) {
        request.setAcceptableLanguages(parseAcceptableLanguages(request.getRequestHeaderValue(BatchConstants.ACCEPT_LANGUAGE)));
      } else {
        request.setAcceptableLanguages(new ArrayList<Locale>());
      }

      parseNewLine(scanner);

      if (isChangeSet) {
        request.setBody(parseBody(scanner));
      }

    } else {
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
    return request;
  }

  private Map<String, String> mapHeaderListToString(final Map<String, List<String>> requestHeaders) {
    Map<String, String> headers = new HashMap<String, String>();
    for (Map.Entry<String, List<String>> header : requestHeaders.entrySet()) {
      String headerName = header.getKey();
      List<String> values = header.getValue();
      String headerValue = null;
      for (String value : values) {
        if (headerValue == null) {
          headerValue = value;
        } else {
          headerValue = headerValue + ";" + value;
        }
      }
      headers.put(headerName, headerValue);
    }
    return headers;
  }

  private Map<String, List<String>> parseRequestHeaders(final Scanner scanner) throws EntityProviderException {
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext(REG_EX_HEADER)) {
        scanner.next(REG_EX_HEADER);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          if (headers.containsKey(headerName)) {
            headers.get(headerName).add(headerValue);
          } else {
            List<String> headerList = new ArrayList<String>();
            headerList.add(headerValue);
            headers.put(headerName, headerList);
          }
        }
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid header: " + scanner.next()));
      }
    }
    return headers;
  }

  private PathInfo parseRequestUri(final String uri) throws EntityProviderException {
    PathInfoImpl pathInfo = new PathInfoImpl();
    pathInfo.setServiceRoot(batchRequestPathInfo.getServiceRoot());
    pathInfo.setPrecedingPathSegment(batchRequestPathInfo.getPrecedingSegments());
    Scanner uriScanner = new Scanner(uri);
    Pattern requestUri = Pattern.compile("(?:" + baseUri + "/)?([^?]+)(\\?.*)?");
    if (uriScanner.hasNext("\\$[^/]/([^?]+)(?:\\?.*)?")) {
      // TODO: Content-ID reference 
      uriScanner.next("\\$[^/]/([^?]+)(?:\\?.*)?");
    } else if (uriScanner.hasNext(requestUri)) {
      uriScanner.next(requestUri);
      MatchResult result = uriScanner.match();
      if (result.groupCount() == 2) {
        String odataPathSegmentsAsString = result.group(1);
        String queryParametersAsString = result.group(2) != null ? result.group(2) : "";
        pathInfo.setODataPathSegment(parseODataPathSegments(odataPathSegmentsAsString));
        try {
          pathInfo.setRequestUri(new URI(baseUri + "/" + odataPathSegmentsAsString + queryParametersAsString));
        } catch (URISyntaxException e) {
          uriScanner.close();
          throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid URI"), e);
        }
      } else {
        uriScanner.close();
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid URI"));
      }
    } else {
      uriScanner.close();
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid URI"));
    }
    uriScanner.close();
    return pathInfo;
  }

  private Map<String, String> parseQueryParameters(final String uri) throws EntityProviderException {
    Scanner uriScanner = new Scanner(uri);
    Map<String, String> queryParametersMap = new HashMap<String, String>();
    Pattern regex = Pattern.compile("(?:" + baseUri + "/)?" + "[^?]+" + "\\?(.*)");
    if (uriScanner.hasNext(regex)) {
      uriScanner.next(regex);
      MatchResult uriResult = uriScanner.match();
      if (uriResult.groupCount() == 1) {
        String queryParams = uriResult.group(1);
        Scanner queryParamsScanner = new Scanner(queryParams).useDelimiter("&");
        while (queryParamsScanner.hasNext(REG_EX_QUERY_PARAMETER)) {
          queryParamsScanner.next(REG_EX_QUERY_PARAMETER);
          MatchResult result = queryParamsScanner.match();
          if (result.groupCount() == 2) {
            String systemQueryOption = result.group(1);
            String value = result.group(2);
            queryParametersMap.put(systemQueryOption, value);
          } else {
            queryParamsScanner.close();
            throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid query parameters"));
          }
        }
        queryParamsScanner.close();

      } else {
        uriScanner.close();
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid URL"));
      }
    }
    uriScanner.close();
    return queryParametersMap;
  }

  private List<PathSegment> parseODataPathSegments(final String odataPathSegmentsAsString) {
    Scanner pathSegmentScanner = new Scanner(odataPathSegmentsAsString).useDelimiter("/");
    List<PathSegment> odataPathSegments = new ArrayList<PathSegment>();
    while (pathSegmentScanner.hasNext()) {
      odataPathSegments.add(new ODataPathSegmentImpl(pathSegmentScanner.next(), null));
    }
    pathSegmentScanner.close();
    return odataPathSegments;
  }

  private List<String> parseAcceptHeaders(final String headerValue) throws EntityProviderException {
    return AcceptParser.parseAcceptHeaders(headerValue);
  }

  private List<Locale> parseAcceptableLanguages(final String headerValue) throws EntityProviderException {
    return AcceptParser.parseAcceptableLanguages(headerValue);
  }

  private InputStream parseBody(final Scanner scanner) {
    String body = null;
    InputStream requestBody = null;
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      if (!scanner.hasNext(REG_EX_ZERO_OR_MORE_WHITESPACES)) {
        if (body == null) {
          body = scanner.next();
        } else {
          body = body + "\n" + scanner.next();
        }
      } else {
        scanner.next();
      }
    }
    if (body != null) {
      requestBody = new ByteArrayInputStream(body.getBytes());
    }
    return requestBody;
  }

  private String getBoundary(final String contentType) throws EntityProviderException {
    Scanner contentTypeScanner = new Scanner(contentType).useDelimiter(";\\s?");
    if (contentTypeScanner.hasNext(REG_EX_CONTENT_TYPE)) {
      contentTypeScanner.next(REG_EX_CONTENT_TYPE);
    } else {
      contentTypeScanner.close();
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Content-Type of the batch request should be " + BatchConstants.MULTIPART_MIXED));
    }
    if (contentTypeScanner.hasNext(REG_EX_BOUNDARY_PARAMETER)) {
      contentTypeScanner.next(REG_EX_BOUNDARY_PARAMETER);
      MatchResult result = contentTypeScanner.match();
      contentTypeScanner.close();
      if (result.groupCount() == 1 && result.group(1).trim().matches(REG_EX_BOUNDARY)) {
        return trimQuota(result.group(1).trim());
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid boundary"));
      }
    } else {
      contentTypeScanner.close();
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("The Content-Type field for multipart entities requires \"boundary\" parameter"));
    }
  }

  private void validateEncoding(final String encoding) throws EntityProviderException {
    if (!BatchConstants.BINARY_ENCODING.equals(encoding)) {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("The Content-Transfer-Encoding should be binary"));
    }
  }

  private Map<String, String> parseHeaders(final Scanner scanner) throws EntityProviderException {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext(REG_EX_HEADER)) {
        scanner.next(REG_EX_HEADER);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          headers.put(headerName, headerValue);
        }
      } else {
        throw new EntityProviderException(EntityProviderException.COMMON.addContent("Invalid header"));
      }
    }
    return headers;
  }

  private void parseNewLine(final Scanner scanner) throws EntityProviderException {
    if (scanner.hasNext("")) {
      scanner.next();
    } else {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("Expected blank line"));
    }
  }

  private String getBaseUri() throws EntityProviderException {
    if (batchRequestPathInfo != null) {
      if (batchRequestPathInfo.getServiceRoot() != null) {
        String baseUri = batchRequestPathInfo.getServiceRoot().toString();
        for (PathSegment precedingPS : batchRequestPathInfo.getPrecedingSegments()) {
          baseUri = baseUri + "/" + precedingPS.getPath();
        }
        return baseUri;
      }
    } else {
      throw new EntityProviderException(EntityProviderException.COMMON.addContent("PathInfo should not be null"));
    }
    return null;
  }

  private String trimQuota(String boundary) {
    if (boundary.matches("\".*\"")) {
      boundary = boundary.replace("\"", "");
    }
    return boundary;
  }
}
