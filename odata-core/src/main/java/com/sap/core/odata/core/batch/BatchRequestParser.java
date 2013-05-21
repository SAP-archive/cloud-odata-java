package com.sap.core.odata.core.batch;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.MatchResult;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.core.ODataRequestImpl;

public class BatchRequestParser {
  private Scanner scanner;
  private String boundary;
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

  public List<Object> parseBatchRequest(final InputStream in) {
    scanner = new Scanner(in).useDelimiter("\n");
    List<Object> requests = new LinkedList<Object>();
    if (scanner.hasNext("POST (.*)\\$batch HTTP(.*)")) {
      scanner.next();
    }
    parseMultipartHeader();
    parseNewLine();
    parseMultipart(boundary, false);
    while (scanner.hasNext()) {
      scanner.next();
    }
    return requests;

  }

  private Object parseMultipart(final String boundary, final boolean isChangeSet) {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    Object object = new Object();
    if (scanner.hasNext("--" + boundary)) {
      scanner.next();

      mimeHeaders = getMimeHeaders();
      parseNewLine();
      String contentType = mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TYPE);
      if (contentType == null) {
        // exception
      }
      if (isChangeSet) {
        if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          object = parseRequest(isChangeSet, mimeHeaders);
        }
      } else {
        if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          validateEncoding(mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING));
          object = parseRequest(isChangeSet, mimeHeaders);
        } else if (contentType.matches(BatchRequestConstants.MULTIPART_MIXED)) {
          String changeSetBoundary = getBoundary(contentType);
          while (!scanner.hasNext("--" + changeSetBoundary + "--")) {
            parseMultipart(changeSetBoundary, true);
          }
        }
      }
    }
    return object;

  }

  private void validateEncoding(final String encoding) {
    if (!BatchRequestConstants.BINARY_ENCODING.equals(encoding)) {
      //throw new Exception();
    }
  }

  private ODataRequest parseRequest(final boolean isChangeSet, final Map<String, String> headers) {
    ODataRequestImpl request = new ODataRequestImpl();
    request.setHeaders(headers);
    if (scanner.hasNext("(GET|POST|PUT|DELETE|MERGE) (.*)")) {
      scanner.next("(GET|POST|PUT|DELETE|MERGE) (.*) HTTP.*");
      String method = null;
      MatchResult result = scanner.match();
      if (result.groupCount() == 2) {
        method = result.group(1);
        result.group(2);
      } else {
        //throw new Exception ("invalid ")
      }
      if (isChangeSet) {
        if (!HTTP_CHANGESET_METHODS.contains(method)) {
          //throw new Exception("Invalid method.  A ChangeSet can contain insert, update or delete requests");
        }
      } else if (!HTTP_BATCH_METHODS.contains(method)) {
        //throw new Exception("Invalid method.  A Batch Request can contain retrieve requests and/or ChangeSets");
      }
      request.setMethod(ODataHttpMethod.valueOf(method));
    }
    return request;
  }

  private Map<String, String> getMimeHeaders() {
    Map<String, String> headers = new HashMap<String, String>();
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext("(.*):(.*)")) {
        scanner.next("(.*):\\s?(.*)\\s*");
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          String headerName = result.group(1).trim();
          String headerValue = result.group(2).trim();
          headers.put(headerName, headerValue);
        }
      }
    }
    return headers;
  }

  private void parseMultipartHeader() {
    while (scanner.hasNext() && !scanner.hasNext("")) {
      if (scanner.hasNext("(.*):(.*)")) {
        scanner.next("(.*):(.*)");
        MatchResult result = scanner.match();
        if (result.groupCount() == 2) {
          if (result.group(1).matches("Content-Type")) {
            boundary = getBoundary(result.group(2));
          }
        }
      }
    }
  }

  private String getBoundary(final String contentType) {
    Scanner contentTypeScanner = new Scanner(contentType).useDelimiter(";");
    if (contentTypeScanner.hasNext("(\\s)?multipart/mixed")) {
      contentTypeScanner.next("(\\s)?multipart/mixed");
    } else {
      System.out.println("Content-Type of the batch request should be multipart/mixed");
    }
    if (contentTypeScanner.hasNext("(\\s)?boundary=(.*)")) {
      contentTypeScanner.next("\\s?boundary=\"?(.*)\"?\\s*");
      MatchResult result = contentTypeScanner.match();
      if (result.groupCount() == 1) {
        return result.group(1).trim();
      }
    } else {
      System.out.println("Invalid boundary of the batch request");
    }
    return "boundary";
  }

  private void parseNewLine() {
    if (scanner.hasNext("")) {
      scanner.next("");
    }
  }
}
