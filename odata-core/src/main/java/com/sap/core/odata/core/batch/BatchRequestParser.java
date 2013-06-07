package com.sap.core.odata.core.batch;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.core.ODataRequestImpl;
import com.sap.core.odata.core.commons.ContentType;

public class BatchRequestParser {
  private static final String ANY_CHARACTERS = ".*";
  private Scanner scanner;
  private String boundary;
  private int linenumber = 0;
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
      throw new ODataException(e.getMessage(), e);
    } finally {
      scanner.close();
    }
    return requestList;
  }

  public List<Object> parseBatchRequest() throws ODataException {
    String closeDelimiter;
    List<Object> requests = new LinkedList<Object>();
    // ....... will be removed ..........//
    if (scanner.hasNext("POST (.*)\\$batch" + OPTIONAL_WHITESPACE_REG_EX + VERSION_REG_EX + "?" + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      scanner.next();
      nextLineNumber();
      parseMultipartHeader();
      parseNewLine();
      // ....... will be removed ..........//
      parsePreamble();
      closeDelimiter = "--" + boundary + "--" + ZERO_OR_MORE_WHITESPACES_REG_EX;
      while (scanner.hasNext() && !scanner.hasNext(closeDelimiter)) {
        requests.add(parseMultipart(boundary, false));
        parseNewLine();
      }
      if (scanner.hasNext(closeDelimiter)) {
        scanner.next(closeDelimiter);
      } else {
        throw new ODataException("The close delimiter is expected: line " + linenumber);
      }
    } else {
      throw new ODataException();
    }
    return requests;

  }

  private void parseMultipartHeader() throws ODataException {
    while (scanner.hasNext() && (!scanner.hasNext("") && (!scanner.hasNext("--" + boundary + ZERO_OR_MORE_WHITESPACES_REG_EX)))) {
      if (scanner.hasNext("[a-zA-Z\\-]+:" + ANY_CHARACTERS)) {
        nextLineNumber();
        scanner.next("([a-zA-Z\\-]+):(.*)" + ZERO_OR_MORE_WHITESPACES_REG_EX);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          if (result.group(1).matches("Content-Type")) {
            boundary = getBoundary(result.group(2));
          }
        }
      } else {
        nextLineNumber();
        throw new ODataException("Invalid multipart header: " + linenumber);
      }
    }
  }

  //The method parses additional information prior to the first boundary delimiter line
  private void parsePreamble() {
    while (scanner.hasNext() && !scanner.hasNext("--" + ANY_CHARACTERS)) {
      scanner.next();
    }
  }

  private Object parseMultipart(final String boundary, final boolean isChangeSet) throws ODataException {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    Object object = null;
    if (scanner.hasNext("--" + boundary + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      scanner.next();
      nextLineNumber();
      mimeHeaders = getHeaders();//NO header fields are required in multipart

      String contentType = mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TYPE);
      if (contentType == null) {
        throw new ODataException("No Content-Type field for MIME-header is present");
      }
      if (isChangeSet) {
        if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          parseNewLine();// mandatory
          object = parseRequest(isChangeSet);
        } else {
          throw new ODataException("Invalid Content-Type field for MIME-header");
        }
      } else {
        if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          parseNewLine();// mandatory
          object = parseRequest(isChangeSet);
        } else if (contentType.matches(OPTIONAL_WHITESPACE_REG_EX + BatchRequestConstants.MULTIPART_MIXED + ANY_CHARACTERS)) {
          String changeSetBoundary = getBoundary(contentType);
          List<Object> changeSetRequests = new LinkedList<Object>();
          parseNewLine();// mandatory
          while (!scanner.hasNext("--" + changeSetBoundary + "--" + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
            changeSetRequests.add(parseMultipart(changeSetBoundary, true));
          }
          scanner.next("--" + changeSetBoundary + "--" + ZERO_OR_MORE_WHITESPACES_REG_EX); // for changeSet close delimiter
          nextLineNumber();
          parseNewLine();
          object = changeSetRequests;
        } else {
          throw new ODataException("Invalid Content-Type: line " + linenumber);
        }
      }
    } else if (scanner.hasNext(boundary + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      nextLineNumber();
      throw new ODataException("The boundary delimiter must begin with two hyphen(\"-\") characters: line " + linenumber);
    } else if (scanner.hasNext("--.*")) {
      nextLineNumber();
      throw new ODataException("The boundary string doesn't match the boundary from Content-Type header field " + boundary + ": line " + linenumber);
    } else {
      nextLineNumber();
      throw new ODataException("The boundary delimiter is expected: line " + linenumber);
    }
    return object;

  }

  private ODataRequest parseRequest(final boolean isChangeSet) throws ODataException {
    ODataRequestImpl request = new ODataRequestImpl();
    if (scanner.hasNext("(GET|POST|PUT|DELETE|MERGE) " + ANY_CHARACTERS + OPTIONAL_WHITESPACE_REG_EX + VERSION_REG_EX + "?" + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      scanner.next("(GET|POST|PUT|DELETE|MERGE) (.*)" + OPTIONAL_WHITESPACE_REG_EX + VERSION_REG_EX + "?" + ZERO_OR_MORE_WHITESPACES_REG_EX);
      nextLineNumber();
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
          throw new ODataException("Invalid method.  A ChangeSet cannot retrieve requests: line " + linenumber);
        }
      } else if (!HTTP_BATCH_METHODS.contains(method)) {
        throw new ODataException("Invalid method.  A Batch Request cannot contain insert, update or delete requests: line " + linenumber);
      }
      request.setMethod(ODataHttpMethod.valueOf(method));
      request.setRequestHeaders(getRequestHeaders());
      request.setHeaders(getHeaders());
      if (request.getHeaderValue(BatchRequestConstants.HTTP_CONTENT_TYPE) != null) {
        request.setContentType(ContentType.create(request.getHeaderValue(BatchRequestConstants.HTTP_CONTENT_TYPE)));
      }
      if (request.getHeaderValue(BatchRequestConstants.ACCEPT) != null) {
        request.setAcceptHeaders(parseAcceptHeaders(request.getHeaderValue(BatchRequestConstants.ACCEPT)));
      }
      if (request.getHeaderValue("Accept-Language") != null) {
        request.setAcceptableLanguages(parseAcceptableLanguages(request.getHeaderValue("Accept-Language")));
      }

      parseNewLine();

      if (isChangeSet) {
        request.setBody(parseBody());
      }

    } else {
      nextLineNumber();
      throw new ODataException("line " + linenumber);
    }
    return request;
  }

  private Map<String, List<String>> getRequestHeaders() throws ODataException {
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext("[a-zA-Z\\-]+:" + ANY_CHARACTERS)) {
        scanner.next("([a-zA-Z\\-]+):" + OPTIONAL_WHITESPACE_REG_EX + "(.*)" + ZERO_OR_MORE_WHITESPACES_REG_EX);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          if(headers.containsKey(headerName)){
            headers.get(headerName).add(headerValue);
          }else{
            List<String> headerList = new ArrayList<String>(); 
            headerList.add(headerValue);
            headers.put(headerName, headerList);
          }
        }
        nextLineNumber();
      } else {
        nextLineNumber();
        throw new ODataException("Invalid header: " + linenumber);
      }
    }
    return headers;
  }

  private List<String> parseAcceptHeaders(final String headerValue) throws ODataException {
    List<String> acceptHeaders = new ArrayList<String>();
    List<Double> acceptParams = new ArrayList<Double>();
    Scanner acceptHeaderScanner = new Scanner(headerValue).useDelimiter(",\\s?");
    while (acceptHeaderScanner.hasNext()) {
      if (acceptHeaderScanner.hasNext("[a-z\\*]+/[a-z\\+\\*\\-]+")) {
        acceptHeaderScanner.next("([a-z\\*]+/[a-z\\+\\*\\-]+)");
        MatchResult result = acceptHeaderScanner.match();
        if (result.groupCount() == 1) {
          String acceptHeader = result.group(1);
          acceptHeaders.add(0, acceptHeader);
          acceptParams.add(0, 1.0);
        } else {
          acceptHeaderScanner.close();
          throw new ODataException("Invalid Accept header: " + acceptHeaderScanner.next());
        }
      } else if (acceptHeaderScanner.hasNext("[a-z\\*]+/[a-z\\+\\*\\-=;]+;q=((1\\.0)|(0\\.[1-9]))")) {
        acceptHeaderScanner.next("([a-z\\*]+/[a-z\\+\\*\\-=;]+);q=((?:1\\.0)|(?:0\\.[1-9]))");
        MatchResult result = acceptHeaderScanner.match();
        if (result.groupCount() == 2) {
          String acceptHeader = result.group(1);
          double qualityFactor = Double.parseDouble(result.group(2));
          if (!acceptParams.isEmpty()) {
            int index = findIndex(qualityFactor, acceptParams);
            acceptHeaders.add(index, acceptHeader);
            acceptParams.add(index, qualityFactor);
          } else {
            acceptHeaders.add(acceptHeader);
            acceptParams.add(qualityFactor);
          }
        } else {
          acceptHeaderScanner.close();
          throw new ODataException("Invalid Accept header: " + acceptHeaderScanner.next());
        }
      } else {
        acceptHeaderScanner.close();
        throw new ODataException("Invalid Accept header: " + acceptHeaderScanner.next());
      }
    }
    acceptHeaderScanner.close();
    return acceptHeaders;
  }

  private List<Locale> parseAcceptableLanguages(final String headerValue) throws ODataException {
    List<Locale> acceptLanguages = new LinkedList<Locale>();
    List<Double> acceptParams = new ArrayList<Double>();
    Locale locale;
    Scanner acceptLanguageScanner = new Scanner(headerValue).useDelimiter(",\\s?");
    while (acceptLanguageScanner.hasNext()) {
      if (acceptLanguageScanner.hasNext("[a-z]{2}(?:\\-[A-Z]{2})?")) {
        acceptLanguageScanner.next("([a-z][a-z])\\-?([A-Z][A-Z])?");
        MatchResult result = acceptLanguageScanner.match();
        if (result.groupCount() == 2) {
          String language = result.group(1);
          String country = result.group(2) != null ? result.group(2) : "";
          locale = new Locale(language, country);
          acceptLanguages.add(0, locale);
          acceptParams.add(0, 1.0);
        } else {
          acceptLanguageScanner.close();
          throw new ODataException("Invalid Accept-Language: " + acceptLanguageScanner.next());
        }
      } else if (acceptLanguageScanner.hasNext("[a-z]{2}(?:\\-[A-Z]{2})?;q=((1\\.0)|(0\\.[1-9]))")) {
        acceptLanguageScanner.next("([a-z][a-z])\\-?([A-Z][A-Z])?;q=((?:1\\.0)|(?:0\\.[1-9]))");
        MatchResult result2 = acceptLanguageScanner.match();
        if (result2.groupCount() == 3) {
          String language = result2.group(1);
          String country = result2.group(2) != null ? result2.group(2) : "";
          locale = new Locale(language, country);
          double qualityFactor = Double.parseDouble(result2.group(3));
          if (!acceptParams.isEmpty()) {
            int index = findIndex(qualityFactor, acceptParams);
            acceptParams.add(index, qualityFactor);
            acceptLanguages.add(index, locale);
          } else {
            acceptLanguages.add(locale);
          }
        } else {
          acceptLanguageScanner.close();
          throw new ODataException("Invalid Accept-Language: " + acceptLanguageScanner.next());
        }
      } else {
        acceptLanguageScanner.close();
        throw new ODataException("Invalid Accept-Language: " + acceptLanguageScanner.next());
      }
    }
    acceptLanguageScanner.close();
    return acceptLanguages;
  }

  private InputStream parseBody() {
    String body = null;
    while (scanner.hasNext() && !scanner.hasNext("--" + ANY_CHARACTERS)) {
      nextLineNumber();
      if (!scanner.hasNext("\\s*")) {
        if (body == null) {
          body = scanner.next();
        } else {
          body = body + "\n" + scanner.next();
        }
      } else {
        scanner.next();
      }
    }
    return new ByteArrayInputStream(body.getBytes());
  }

  private String getBoundary(final String contentType) throws ODataException {
    Scanner contentTypeScanner = new Scanner(contentType).useDelimiter(";\\s?");
    if (contentTypeScanner.hasNext(OPTIONAL_WHITESPACE_REG_EX + BatchRequestConstants.MULTIPART_MIXED)) {
      contentTypeScanner.next(OPTIONAL_WHITESPACE_REG_EX + BatchRequestConstants.MULTIPART_MIXED);
    } else {
      contentTypeScanner.close();
      throw new ODataException("Content-Type of the batch request should be " + BatchRequestConstants.MULTIPART_MIXED + " line: " + linenumber);
    }
    if (contentTypeScanner.hasNext(OPTIONAL_WHITESPACE_REG_EX + "boundary=" + ANY_CHARACTERS + ZERO_OR_MORE_WHITESPACES_REG_EX)) {
      contentTypeScanner.next(OPTIONAL_WHITESPACE_REG_EX + "boundary=(\".*\"|.*)" + ZERO_OR_MORE_WHITESPACES_REG_EX);
      MatchResult result = contentTypeScanner.match();
      contentTypeScanner.close();
      if (result.groupCount() == 1 && result.group(1).trim().matches(BOUNDARY_REG_EX)) {
        return trimQuota(result.group(1).trim());
      } else {
        throw new ODataException("Invalid boundary: line " + linenumber);
      }
    } else {
      contentTypeScanner.close();
      throw new ODataException("The Content-Type field for multipart entities requires \"boundary\" parameter: line " + linenumber);
    }
  }

  private void validateEncoding(final String encoding) throws ODataException {
    if (!BatchRequestConstants.BINARY_ENCODING.equals(encoding)) {
      throw new ODataException("The Content-Transfer-Encoding isn't binary");
    }
  }

  private Map<String, String> getHeaders() throws ODataException {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext("[a-zA-Z\\-]+:" + ANY_CHARACTERS)) {
        scanner.next("([a-zA-Z\\-]+):" + OPTIONAL_WHITESPACE_REG_EX + "(.*)" + ZERO_OR_MORE_WHITESPACES_REG_EX);
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          headers.put(headerName, headerValue);
        }
        nextLineNumber();
      } else {
        nextLineNumber();
        throw new ODataException("Invalid header: " + linenumber);
      }
    }
    return headers;
  }

  private void parseNewLine() throws ODataException {
    if (scanner.hasNext("")) {
      scanner.next();
      nextLineNumber();
    }
  }

  private void nextLineNumber() {
    linenumber++;
  }

  private String trimQuota(String boundary) {
    if (boundary.matches("\".*\"")) {
      boundary = boundary.substring(1, boundary.length() - 1);
    }
    return boundary;
  }

  private int findIndex(final double qualityFactor, final List<Double> params) {
    int index = 0;
    boolean isSmaller = true;
    while (index < params.size() && isSmaller) {
      double q = params.get(index);
      if (q <= qualityFactor) {
        isSmaller = false;
      } else {
        index++;
      }
    }
    return index;
  }
}
