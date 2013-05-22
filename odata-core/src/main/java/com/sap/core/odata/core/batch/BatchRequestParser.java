package com.sap.core.odata.core.batch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.core.ODataRequestImpl;
import com.sap.core.odata.core.commons.ContentType;

public class BatchRequestParser {
  private static final String ANY_CHARACTERS = ".*";
  private Scanner scanner;
  private String boundary;
  private static final String BOUNDARY_REG_EX = "([a-zA-Z0-9_\\-\\.'\\+]{1,70})|\"([a-zA-Z0-9_\\-\\.'\\+\\s\\(\\),/:=\\?]{1,69}[a-zA-Z0-9_\\-\\.'\\+\\(\\),/:=\\?])\""; // See RFC 2046
  private static final String VERSION_REG_EX = "HTTP/[0-9]\\.[0-9]";
  private static final String OPTIONAL_WHITESPACE_REG_EX = "\\s?";
  private static final String ZERO_OR_MORE_WHITESPACES_REG_EX = "\\s*";
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

  public List<Object> parse(final InputStream in) throws ODataException {
    scanner = new Scanner(in).useDelimiter("\n");
    List<Object> requestList;
    try {
      requestList = parseBatchRequest();
    } catch (ODataException e) {
      throw new ODataException(e.getMessage());
    } finally {
      scanner.close();
    }
    return requestList;
  }

  public List<Object> parseBatchRequest() throws ODataException {
    String closeDelimiter;

    List<Object> requests = new LinkedList<Object>();
    if (scanner.hasNext("POST (.*)\\$batch" + OPTIONAL_WHITESPACE_REG_EX + VERSION_REG_EX + "?" + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      scanner.next();
      parseMultipartHeader();
      parseNewLine();

      closeDelimiter = "--" + boundary + "--" + ZERO_OR_MORE_WHITESPACES_REG_EX;
      while (scanner.hasNext() && !scanner.hasNext(closeDelimiter)) {
        requests.add(parseMultipart(boundary, false));
        parseNewLine();
      }
      scanner.next(closeDelimiter);
    } else {
      throw new ODataException();
    }
    return requests;

  }

  private Object parseMultipart(final String boundary, final boolean isChangeSet) throws ODataException {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    Object object = null;
    if (scanner.hasNext("--" + boundary + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      scanner.next();
      mimeHeaders = getHeaders();
      parseNewLine();
      String contentType = mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TYPE);
      if (contentType == null) {
        throw new ODataException("Missing content-Type");
      }
      if (isChangeSet) {
        if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          object = parseRequest(isChangeSet);
        }
      } else {
        if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          object = parseRequest(isChangeSet);
        } else if (contentType.matches(OPTIONAL_WHITESPACE_REG_EX + BatchRequestConstants.MULTIPART_MIXED + ANY_CHARACTERS)) {
          String changeSetBoundary = getBoundary(contentType);
          List<Object> changeSetRequests = new LinkedList<Object>();
          while (!scanner.hasNext("--" + changeSetBoundary + "--\\s*")) {
            changeSetRequests.add(parseMultipart(changeSetBoundary, true));
          }
          scanner.next("--" + changeSetBoundary + "--" + ZERO_OR_MORE_WHITESPACES_REG_EX); // for changeSet close delimiter
          parseNewLine();
          object = changeSetRequests;
        }
      }
    }
    return object;

  }

  private void validateEncoding(final String encoding) throws ODataException {
    if (!BatchRequestConstants.BINARY_ENCODING.equals(encoding)) {
      throw new ODataException();
    }
  }

  private ODataRequest parseRequest(final boolean isChangeSet) throws ODataException {
    ODataRequestImpl request = new ODataRequestImpl();
    if (scanner.hasNext("(GET|POST|PUT|DELETE|MERGE) " + ANY_CHARACTERS + OPTIONAL_WHITESPACE_REG_EX + VERSION_REG_EX + "?" + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      scanner.next("(GET|POST|PUT|DELETE|MERGE) (.*)" + OPTIONAL_WHITESPACE_REG_EX + VERSION_REG_EX + "?" + ZERO_OR_MORE_WHITESPACES_REG_EX);
      String method = null;
      MatchResult result = scanner.match();
      if (result.groupCount() == 2) {
        method = result.group(1);
        result.group(2);
      } else {
        throw new ODataException();
      }
      if (isChangeSet) {
        if (!HTTP_CHANGESET_METHODS.contains(method)) {
          throw new ODataException("Invalid method.  A ChangeSet cannot retrieve requests");
        }
      } else if (!HTTP_BATCH_METHODS.contains(method)) {
        throw new ODataException("Invalid method.  A Batch Request cannot contain insert, update or delete requests");
      }
      request.setMethod(ODataHttpMethod.valueOf(method));
      request.setHeaders(getHeaders());
      if (request.getHeaderValue(BatchRequestConstants.HTTP_CONTENT_TYPE) != null) {
        request.setContentType(ContentType.create(request.getHeaderValue(BatchRequestConstants.HTTP_CONTENT_TYPE)));
      }
      if (request.getHeaderValue(BatchRequestConstants.ACCEPT) != null) {
        request.setAcceptHeaders(parseAcceptHeaders(request.getHeaderValue(BatchRequestConstants.ACCEPT)));
      }

      parseNewLine();

      if (isChangeSet) {
        request.setBody(parseBody());
      }
    } else {
      throw new ODataException(scanner.next());
    }
    return request;
  }

  private List<String> parseAcceptHeaders(final String headerValue) {
    List<String> acceptHeaders = new ArrayList<String>();
    Scanner acceptHeaderScanner = new Scanner(headerValue).useDelimiter(",\\s?");
    while (acceptHeaderScanner.hasNext()) {
      acceptHeaders.add(acceptHeaderScanner.next());
    }
    acceptHeaderScanner.close();
    return acceptHeaders;
  }

  private InputStream parseBody() {
    String body = null;
    while (scanner.hasNext() && !scanner.hasNext("--" + ANY_CHARACTERS)) {
      if (!scanner.next().isEmpty()) {
        if (body == null) {
          body = scanner.next();
        } else {
          body = body + "\n" + scanner.next();
        }
      }
    }
    return new ByteArrayInputStream(body.getBytes());
  }

  private void parseMultipartHeader() throws ODataException {
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext("[a-zA-Z\\-]+:" + ANY_CHARACTERS)) {
        scanner.next("(.*):(.*)" + ZERO_OR_MORE_WHITESPACES_REG_EX);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          if (result.group(1).matches("Content-Type")) {
            boundary = getBoundary(result.group(2));
          }
        }
      } else {
        throw new ODataException("Invalid multipart header: " + scanner.next());
      }
    }
  }

  private String getBoundary(final String contentType) throws ODataException {
    Scanner contentTypeScanner = new Scanner(contentType).useDelimiter(";\\s?");
    if (contentTypeScanner.hasNext(OPTIONAL_WHITESPACE_REG_EX + BatchRequestConstants.MULTIPART_MIXED)) {
      contentTypeScanner.next(OPTIONAL_WHITESPACE_REG_EX + BatchRequestConstants.MULTIPART_MIXED);
    } else {
      contentTypeScanner.close();
      System.out.println("Content-Type of the batch request should be " + BatchRequestConstants.MULTIPART_MIXED);
    }
    if (contentTypeScanner.hasNext(OPTIONAL_WHITESPACE_REG_EX + "boundary=" + ANY_CHARACTERS + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      contentTypeScanner.next(OPTIONAL_WHITESPACE_REG_EX + "boundary=\"?(.*)\"?" + ZERO_OR_MORE_WHITESPACES_REG_EX);
      MatchResult result = contentTypeScanner.match();
      contentTypeScanner.close();
      if (result.groupCount() == 1 && result.group(1).trim().matches(BOUNDARY_REG_EX)) {
        return result.group(1).trim();
      } else {
        System.out.println("Invalid boundary of the batch request " + contentTypeScanner.next());
        throw new ODataException();
      }
    } else {
      contentTypeScanner.close();
      System.out.println("Invalid boundary of the batch request " + contentTypeScanner.next());
      throw new ODataException();
    }
  }

  private void parseNewLine() {
    if (scanner.hasNext("")) {
      scanner.next();
    }
  }

  private Map<String, String> getHeaders() throws ODataException {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext("[a-zA-Z\\-]+:" + ANY_CHARACTERS)) {
        scanner.next("(.*):" + OPTIONAL_WHITESPACE_REG_EX + "(.*)" + ZERO_OR_MORE_WHITESPACES_REG_EX);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          headers.put(headerName, headerValue);
        }
      } else {
        throw new ODataException("Invalid header: " + scanner.next());
      }
    }
    return headers;
  }
}
