package com.sap.core.odata.core.batch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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

import com.sap.core.odata.api.batch.BatchException;
import com.sap.core.odata.api.batch.BatchRequestPart;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.ep.EntityProviderBatchProperties;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataRequest.ODataRequestBuilder;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.PathInfoImpl;
import com.sap.core.odata.core.commons.Decoder;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * @author SAP AG
 */
public class BatchRequestParser {
  private static final String LF = "\n";
  private static final String REG_EX_OPTIONAL_WHITESPACE = "\\s?";
  private static final String REG_EX_ZERO_OR_MORE_WHITESPACES = "\\s*";
  private static final String ANY_CHARACTERS = ".*";

  private static final Pattern REG_EX_BLANK_LINE = Pattern.compile("(|" + REG_EX_ZERO_OR_MORE_WHITESPACES + ")");
  private static final Pattern REG_EX_HEADER = Pattern.compile("([a-zA-Z\\-]+):" + REG_EX_OPTIONAL_WHITESPACE + "(.*)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_VERSION = Pattern.compile("(?:HTTP/[0-9]\\.[0-9])");
  private static final Pattern REG_EX_ANY_BOUNDARY_STRING = Pattern.compile("--" + ANY_CHARACTERS + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_REQUEST_LINE = Pattern.compile("(GET|POST|PUT|DELETE|MERGE|PATCH)\\s(.*)\\s?" + REG_EX_VERSION + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_BOUNDARY_PARAMETER = Pattern.compile(REG_EX_OPTIONAL_WHITESPACE + "boundary=(\".*\"|.*)" + REG_EX_ZERO_OR_MORE_WHITESPACES);
  private static final Pattern REG_EX_CONTENT_TYPE = Pattern.compile(REG_EX_OPTIONAL_WHITESPACE + HttpContentType.MULTIPART_MIXED);
  private static final Pattern REG_EX_QUERY_PARAMETER = Pattern.compile("((?:\\$|)[^=]+)=([^=]+)");

  private static final String REG_EX_BOUNDARY = "([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+\\s\\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\""; // See RFC 2046
  private String baseUri;
  private PathInfo batchRequestPathInfo;
  private String contentTypeMime;
  private String boundary;
  private String currentMimeHeaderContentId;
  private int currentLineNumber = 0;
  private final static Set<String> HTTP_CHANGESET_METHODS;
  private final static Set<String> HTTP_BATCH_METHODS;

  static {
    HashSet<String> httpChangesetMethods = new HashSet<String>();
    httpChangesetMethods.add("POST");
    httpChangesetMethods.add("PUT");
    httpChangesetMethods.add("DELETE");
    httpChangesetMethods.add("MERGE");
    httpChangesetMethods.add("PATCH");
    HTTP_CHANGESET_METHODS = Collections.unmodifiableSet(httpChangesetMethods);

    HashSet<String> httpBatchMethods = new HashSet<String>();
    httpBatchMethods.add("GET");
    HTTP_BATCH_METHODS = Collections.unmodifiableSet(httpBatchMethods);
  }

  public BatchRequestParser(final String contentType, final EntityProviderBatchProperties properties) {
    contentTypeMime = contentType;
    batchRequestPathInfo = properties.getPathInfo();
  }

  public List<BatchRequestPart> parse(final InputStream in) throws BatchException {
    Scanner scanner = new Scanner(in, BatchHelper.DEFAULT_ENCODING).useDelimiter(LF);
    baseUri = getBaseUri();
    List<BatchRequestPart> requestList;
    try {
      requestList = parseBatchRequest(scanner);
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      scanner.close();
      try {
        in.close();
      } catch (IOException e) {
        throw new ODataRuntimeException(e);
      }
    }
    return requestList;
  }

  private List<BatchRequestPart> parseBatchRequest(final Scanner scanner) throws BatchException {
    List<BatchRequestPart> requests = new LinkedList<BatchRequestPart>();
    if (contentTypeMime != null) {
      boundary = getBoundary(contentTypeMime);
      parsePreamble(scanner);
      final String closeDelimiter = "--" + boundary + "--" + REG_EX_ZERO_OR_MORE_WHITESPACES;
      while (scanner.hasNext() && !scanner.hasNext(closeDelimiter)) {
        requests.add(parseMultipart(scanner, boundary, false));
        parseNewLine(scanner);
      }
      if (scanner.hasNext(closeDelimiter)) {
        scanner.next(closeDelimiter);
        currentLineNumber++;
      } else {
        throw new BatchException(BatchException.MISSING_CLOSE_DELIMITER.addContent(currentLineNumber));
      }
    } else {
      throw new BatchException(BatchException.MISSING_CONTENT_TYPE);
    }
    return requests;
  }

  //The method parses additional information prior to the first boundary delimiter line
  private void parsePreamble(final Scanner scanner) {
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      scanner.next();
      currentLineNumber++;
    }
  }

  private BatchRequestPart parseMultipart(final Scanner scanner, final String boundary, final boolean isChangeSet) throws BatchException {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    BatchRequestPart multipart = null;
    List<ODataRequest> requests = new ArrayList<ODataRequest>();
    if (scanner.hasNext("--" + boundary + REG_EX_ZERO_OR_MORE_WHITESPACES)) {
      scanner.next();
      currentLineNumber++;
      mimeHeaders = parseHeaders(scanner);
      currentMimeHeaderContentId = mimeHeaders.get(BatchHelper.HTTP_CONTENT_ID.toLowerCase(Locale.ENGLISH));

      String contentType = mimeHeaders.get(HttpHeaders.CONTENT_TYPE.toLowerCase(Locale.ENGLISH));
      if (contentType == null) {
        throw new BatchException(BatchException.MISSING_CONTENT_TYPE);
      }
      if (isChangeSet) {
        if (HttpContentType.APPLICATION_HTTP.equalsIgnoreCase(contentType)) {
          validateEncoding(mimeHeaders.get(BatchHelper.HTTP_CONTENT_TRANSFER_ENCODING.toLowerCase(Locale.ENGLISH)));
          parseNewLine(scanner);// mandatory

          requests.add(parseRequest(scanner, isChangeSet));
          multipart = new BatchRequestPartImpl(false, requests);
        } else {
          throw new BatchException(BatchException.INVALID_CONTENT_TYPE.addContent(HttpContentType.APPLICATION_HTTP));
        }
      } else {
        if (HttpContentType.APPLICATION_HTTP.equalsIgnoreCase(contentType)) {
          validateEncoding(mimeHeaders.get(BatchHelper.HTTP_CONTENT_TRANSFER_ENCODING.toLowerCase(Locale.ENGLISH)));
          parseNewLine(scanner);// mandatory
          requests.add(parseRequest(scanner, isChangeSet));
          multipart = new BatchRequestPartImpl(false, requests);
        } else if (contentType.matches(REG_EX_OPTIONAL_WHITESPACE + HttpContentType.MULTIPART_MIXED + ANY_CHARACTERS)) {
          String changeSetBoundary = getBoundary(contentType);
          if (boundary.equals(changeSetBoundary)) {
            throw new BatchException(BatchException.INVALID_CHANGESET_BOUNDARY.addContent(currentLineNumber));
          }
          List<ODataRequest> changeSetRequests = new LinkedList<ODataRequest>();
          parseNewLine(scanner);// mandatory
          Pattern changeSetCloseDelimiter = Pattern.compile("--" + changeSetBoundary + "--" + REG_EX_ZERO_OR_MORE_WHITESPACES);
          while (!scanner.hasNext(changeSetCloseDelimiter)) {
            BatchRequestPart part = parseMultipart(scanner, changeSetBoundary, true);
            changeSetRequests.addAll(part.getRequests());
          }
          scanner.next(changeSetCloseDelimiter);
          currentLineNumber++;
          multipart = new BatchRequestPartImpl(true, changeSetRequests);
        } else {
          throw new BatchException(BatchException.INVALID_CONTENT_TYPE.addContent(HttpContentType.MULTIPART_MIXED + " or " + HttpContentType.APPLICATION_HTTP));
        }
      }
    } else if (scanner.hasNext(boundary + REG_EX_ZERO_OR_MORE_WHITESPACES)) {
      currentLineNumber++;
      throw new BatchException(BatchException.INVALID_BOUNDARY_DELIMITER.addContent(currentLineNumber));
    } else if (scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      currentLineNumber++;
      throw new BatchException(BatchException.NO_MATCH_WITH_BOUNDARY_STRING.addContent(boundary).addContent(currentLineNumber));
    } else {
      currentLineNumber++;
      throw new BatchException(BatchException.MISSING_BOUNDARY_DELIMITER.addContent(currentLineNumber));
    }
    return multipart;

  }

  private ODataRequest parseRequest(final Scanner scanner, final boolean isChangeSet) throws BatchException {
    if (scanner.hasNext(REG_EX_REQUEST_LINE)) {
      scanner.next(REG_EX_REQUEST_LINE);
      currentLineNumber++;
      final String method;
      final String uri;
      MatchResult result = scanner.match();
      if (result.groupCount() == 2) {
        method = result.group(1);
        uri = result.group(2).trim();
      } else {
        currentLineNumber++;
        throw new BatchException(BatchException.INVALID_REQUEST_LINE.addContent(scanner.next()).addContent(currentLineNumber));
      }
      PathInfo pathInfo = parseRequestUri(uri);
      Map<String, String> queryParameters = parseQueryParameters(uri);
      if (isChangeSet) {
        if (!HTTP_CHANGESET_METHODS.contains(method)) {
          throw new BatchException(BatchException.INVALID_CHANGESET_METHOD.addContent(currentLineNumber));
        }
      } else if (!HTTP_BATCH_METHODS.contains(method)) {
        throw new BatchException(BatchException.INVALID_QUERY_OPERATION_METHOD.addContent(currentLineNumber));
      }
      ODataHttpMethod httpMethod = ODataHttpMethod.valueOf(method);
      Map<String, List<String>> headers = parseRequestHeaders(scanner);
      if (currentMimeHeaderContentId != null) {
        List<String> headerList = new ArrayList<String>();
        headerList.add(currentMimeHeaderContentId);
        headers.put(BatchHelper.MIME_HEADER_CONTENT_ID.toLowerCase(Locale.ENGLISH), headerList);
      }

      String contentType = getContentTypeHeader(headers);
      List<String> acceptHeaders = getAcceptHeader(headers);
      List<Locale> acceptLanguages = getAcceptLanguageHeader(headers);
      parseNewLine(scanner);
      InputStream body = new ByteArrayInputStream(new byte[0]);
      if (isChangeSet) {
        body = parseBody(scanner);
      }

      ODataRequestBuilder requestBuilder = ODataRequest.method(httpMethod)
          .queryParameters(queryParameters)
          .requestHeaders(headers)
          .pathInfo(pathInfo)
          .acceptableLanguages(acceptLanguages)
          .body(body)
          .acceptHeaders(acceptHeaders);

      if (contentType != null) {
        requestBuilder = requestBuilder.contentType(contentType);
      }
      return requestBuilder.build();
    } else {
      currentLineNumber++;
      throw new BatchException(BatchException.INVALID_REQUEST_LINE.addContent(scanner.next()).addContent(currentLineNumber));
    }

  }

  private Map<String, List<String>> parseRequestHeaders(final Scanner scanner) throws BatchException {
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    while (scanner.hasNext() && !scanner.hasNext(REG_EX_BLANK_LINE)) {
      if (scanner.hasNext(REG_EX_HEADER)) {
        scanner.next(REG_EX_HEADER);
        currentLineNumber++;
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim().toLowerCase(Locale.ENGLISH);
          String headerValue = result.group(2).trim();
          if (HttpHeaders.ACCEPT.equalsIgnoreCase(headerName)) {
            List<String> acceptHeaders = parseAcceptHeaders(headerValue);
            headers.put(headerName, acceptHeaders);
          } else if (HttpHeaders.ACCEPT_LANGUAGE.equalsIgnoreCase(headerName)) {
            List<String> acceptLanguageHeaders = parseAcceptableLanguages(headerValue);
            headers.put(headerName, acceptLanguageHeaders);
          } else if (!BatchHelper.HTTP_CONTENT_ID.equalsIgnoreCase(headerName)) {
            if (headers.containsKey(headerName)) {
              headers.get(headerName).add(headerValue);
            } else {
              List<String> headerList = new ArrayList<String>();
              headerList.add(headerValue);
              headers.put(headerName, headerList);
            }
          } else {
            List<String> headerList = new ArrayList<String>();
            headerList.add(headerValue);
            headers.put(BatchHelper.REQUEST_HEADER_CONTENT_ID.toLowerCase(Locale.ENGLISH), headerList);
          }
        }
      } else {
        currentLineNumber++;
        throw new BatchException(BatchException.INVALID_HEADER.addContent(scanner.next()).addContent(currentLineNumber));
      }
    }
    return headers;
  }

  private PathInfo parseRequestUri(final String uri) throws BatchException {
    PathInfoImpl pathInfo = new PathInfoImpl();
    pathInfo.setServiceRoot(batchRequestPathInfo.getServiceRoot());
    pathInfo.setPrecedingPathSegment(batchRequestPathInfo.getPrecedingSegments());
    final String odataPathSegmentsAsString;
    final String queryParametersAsString;
    try {
      Scanner uriScanner = new Scanner(uri).useDelimiter(LF);
      URI uriObject = new URI(uri);
      if (uriObject.isAbsolute()) {
        Pattern regexRequestUri = Pattern.compile(baseUri + "/([^/][^?]*)(\\?.*)?");
        if (uriScanner.hasNext(regexRequestUri)) {
          uriScanner.next(regexRequestUri);
          MatchResult result = uriScanner.match();
          if (result.groupCount() == 2) {
            odataPathSegmentsAsString = result.group(1);
            queryParametersAsString = result.group(2) != null ? result.group(2) : "";
          } else {
            uriScanner.close();
            throw new BatchException(BatchException.INVALID_URI.addContent(currentLineNumber));
          }
        } else {
          uriScanner.close();
          throw new BatchException(BatchException.INVALID_URI.addContent(currentLineNumber));
        }
      } else {
        Pattern regexRequestUri = Pattern.compile("([^/][^?]*)(\\?.*)?");
        if (uriScanner.hasNext(regexRequestUri)) {
          uriScanner.next(regexRequestUri);
          MatchResult result = uriScanner.match();
          if (result.groupCount() == 2) {
            odataPathSegmentsAsString = result.group(1);
            queryParametersAsString = result.group(2) != null ? result.group(2) : "";
          } else {
            uriScanner.close();
            throw new BatchException(BatchException.INVALID_URI.addContent(currentLineNumber));
          }
        } else if (uriScanner.hasNext("/(.*)")) {
          uriScanner.close();
          throw new BatchException(BatchException.UNSUPPORTED_ABSOLUTE_PATH.addContent(currentLineNumber));
        } else {
          uriScanner.close();
          throw new BatchException(BatchException.INVALID_URI.addContent(currentLineNumber));
        }

      }
      uriScanner.close();
      pathInfo.setODataPathSegment(parseODataPathSegments(odataPathSegmentsAsString));
      if (!odataPathSegmentsAsString.startsWith("$")) {
        String requestUri = baseUri + "/" + odataPathSegmentsAsString + queryParametersAsString;
        pathInfo.setRequestUri(new URI(requestUri));
      }
      return pathInfo;
    } catch (URISyntaxException e) {
      throw new BatchException(BatchException.INVALID_URI.addContent(currentLineNumber), e);
    }

  }

  private Map<String, String> parseQueryParameters(final String uri) throws BatchException {
    Scanner uriScanner = new Scanner(uri).useDelimiter("\n");
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
            queryParametersMap.put(systemQueryOption, Decoder.decode(value));
          } else {
            queryParamsScanner.close();
            throw new BatchException(BatchException.INVALID_QUERY_PARAMETER);
          }
        }
        queryParamsScanner.close();

      } else {
        uriScanner.close();
        throw new BatchException(BatchException.INVALID_URI.addContent(currentLineNumber));
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

  private List<String> parseAcceptHeaders(final String headerValue) throws BatchException {
    return AcceptParser.parseAcceptHeaders(headerValue);
  }

  private List<String> parseAcceptableLanguages(final String headerValue) throws BatchException {
    return AcceptParser.parseAcceptableLanguages(headerValue);
  }

  private InputStream parseBody(final Scanner scanner) {
    StringBuilder body = null;
    final InputStream requestBody;

    while (scanner.hasNext() && !scanner.hasNext(REG_EX_ANY_BOUNDARY_STRING)) {
      if (!scanner.hasNext(REG_EX_ZERO_OR_MORE_WHITESPACES)) {
        if (body == null) {
          body = new StringBuilder(scanner.next());
        } else {
          body.append(LF).append(scanner.next());
        }
      } else {
        scanner.next();
      }
      currentLineNumber++;
    }

    if (body != null) {
      requestBody = new ByteArrayInputStream(BatchHelper.getBytes(body.toString()));
    } else {
      requestBody = new ByteArrayInputStream(new byte[0]);
    }
    return requestBody;
  }

  private String getBoundary(final String contentType) throws BatchException {
    Scanner contentTypeScanner = new Scanner(contentType).useDelimiter(";\\s?");
    if (contentTypeScanner.hasNext(REG_EX_CONTENT_TYPE)) {
      contentTypeScanner.next(REG_EX_CONTENT_TYPE);
    } else {
      contentTypeScanner.close();
      throw new BatchException(BatchException.INVALID_CONTENT_TYPE.addContent(HttpContentType.MULTIPART_MIXED));
    }
    if (contentTypeScanner.hasNext(REG_EX_BOUNDARY_PARAMETER)) {
      contentTypeScanner.next(REG_EX_BOUNDARY_PARAMETER);
      MatchResult result = contentTypeScanner.match();
      contentTypeScanner.close();
      if (result.groupCount() == 1 && result.group(1).trim().matches(REG_EX_BOUNDARY)) {
        return trimQuota(result.group(1).trim());
      } else {
        throw new BatchException(BatchException.INVALID_BOUNDARY);
      }
    } else {
      contentTypeScanner.close();
      throw new BatchException(BatchException.MISSING_PARAMETER_IN_CONTENT_TYPE);
    }
  }

  private void validateEncoding(final String encoding) throws BatchException {
    if (!BatchHelper.BINARY_ENCODING.equalsIgnoreCase(encoding)) {
      throw new BatchException(BatchException.INVALID_CONTENT_TRANSFER_ENCODING);
    }
  }

  private Map<String, String> parseHeaders(final Scanner scanner) throws BatchException {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !(scanner.hasNext(REG_EX_BLANK_LINE))) {
      if (scanner.hasNext(REG_EX_HEADER)) {
        scanner.next(REG_EX_HEADER);
        currentLineNumber++;
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim().toLowerCase(Locale.ENGLISH);
          String headerValue = result.group(2).trim();
          headers.put(headerName, headerValue);
        }
      } else {
        throw new BatchException(BatchException.INVALID_HEADER.addContent(scanner.next()));
      }
    }
    return headers;
  }

  private void parseNewLine(final Scanner scanner) throws BatchException {
    if (scanner.hasNext() && scanner.hasNext(REG_EX_BLANK_LINE)) {
      scanner.next();
      currentLineNumber++;
    } else {
      currentLineNumber++;
      if (scanner.hasNext()) {
        throw new BatchException(BatchException.MISSING_BLANK_LINE.addContent(scanner.next()).addContent(currentLineNumber));
      } else {
        throw new BatchException(BatchException.TRUNCATED_BODY.addContent(currentLineNumber));

      }
    }
  }

  private String getBaseUri() throws BatchException {
    if (batchRequestPathInfo != null) {
      if (batchRequestPathInfo.getServiceRoot() != null) {
        String baseUri = batchRequestPathInfo.getServiceRoot().toASCIIString();
        if (baseUri.lastIndexOf('/') == baseUri.length() - 1) {
          baseUri = baseUri.substring(0, baseUri.length() - 1);
        }
        for (PathSegment precedingPS : batchRequestPathInfo.getPrecedingSegments()) {
          baseUri = baseUri + "/" + precedingPS.getPath();
        }
        return baseUri;
      }
    } else {
      throw new BatchException(BatchException.INVALID_PATHINFO);
    }
    return null;
  }

  private String trimQuota(String boundary) {
    if (boundary.matches("\".*\"")) {
      boundary = boundary.replace("\"", "");
    }
    boundary = boundary.replaceAll("\\)", "\\\\)");
    boundary = boundary.replaceAll("\\(", "\\\\(");
    boundary = boundary.replaceAll("\\?", "\\\\?");
    boundary = boundary.replaceAll("\\+", "\\\\+");
    return boundary;
  }

  private List<String> getAcceptHeader(final Map<String, List<String>> headers) {
    List<String> acceptHeaders = new ArrayList<String>();
    List<String> requestAcceptHeaderList = headers.get(HttpHeaders.ACCEPT.toLowerCase(Locale.ENGLISH));

    if (requestAcceptHeaderList != null) {
      acceptHeaders = requestAcceptHeaderList;
    }
    return acceptHeaders;
  }

  private List<Locale> getAcceptLanguageHeader(final Map<String, List<String>> headers) {
    List<String> requestAcceptLanguageList = headers.get(HttpHeaders.ACCEPT_LANGUAGE.toLowerCase(Locale.ENGLISH));
    List<Locale> acceptLanguages = new ArrayList<Locale>();
    if (requestAcceptLanguageList != null) {
      for (String acceptLanguage : requestAcceptLanguageList) {
        String[] part = acceptLanguage.split("-");
        String language = part[0];
        String country = "";
        if (part.length == 2) {
          country = part[part.length - 1];
        }
        Locale locale = new Locale(language, country);
        acceptLanguages.add(locale);
      }
    }
    return acceptLanguages;
  }

  private String getContentTypeHeader(final Map<String, List<String>> headers) {
    List<String> requestContentTypeList = headers.get(HttpHeaders.CONTENT_TYPE.toLowerCase(Locale.ENGLISH));
    String contentType = null;
    if (requestContentTypeList != null) {
      for (String requestContentType : requestContentTypeList) {
        contentType = contentType != null ? contentType + "," + requestContentType : requestContentType;
      }
    }
    return contentType;
  }
}
