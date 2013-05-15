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

import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.core.ODataRequestImpl;

public class BatchRequestParser {
  private boolean isChangeSet;
  private Scanner scanner;
  private String boundary;
  private static Set<String> HTTP_CHANGESET_METHODS;
  static {
    HTTP_CHANGESET_METHODS = new HashSet<String>();
    HTTP_CHANGESET_METHODS.add("POST");
    HTTP_CHANGESET_METHODS.add("PUT");
    HTTP_CHANGESET_METHODS.add("DELETE");
    HTTP_CHANGESET_METHODS.add("MERGE");
  }

  public List<Object> parseBatchRequest(final InputStream in) {
    scanner = new Scanner(in).useDelimiter("\n");
    List<Object> requests = new LinkedList<Object>();
    if (scanner.hasNext("POST (.*)batch HTTP(.*)")) {
      System.out.println(scanner.next("POST (.*)batch HTTP/(.*)"));
    }
    parseMultipartHeader();
    parseNewLine();
    parseMultipart(boundary, false);
    while (scanner.hasNext()) {
      scanner.next();
    }
    return requests;

  }

  private Object parseMultipart(final String boundary, boolean isChangeSet) {
    Map<String, String> mimeHeaders = new HashMap<String, String>();
    Object object= new Object();
    if (scanner.hasNext("--" + boundary)) {
      scanner.next("--" + boundary);
    }
    mimeHeaders = getMimeHeaders();
    
    String contentType = mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TYPE);
    if (contentType == null) {
      // exception
    }
    if (isChangeSet) {
      if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          object = parseRequest();
      }
    } else {
      if (BatchRequestConstants.HTTP_APPLICATION_HTTP.equals(contentType)) {
          object = parseRequest();
      } else if (contentType.matches(BatchRequestConstants.MULTIPART_MIXED)) {
        String changeSetBoundary = getBoundary(contentType);
        while (!scanner.hasNext("--" + changeSetBoundary + "--")) {
          parseMultipart(changeSetBoundary, true);
        }
      }
    }
    return new Object();

  }

  private ODataRequest parseRequest() {
    // TODO Auto-generated method stub
    return new ODataRequestImpl();
  }

  private Map<String, String> getMimeHeaders() {
    // TODO Auto-generated method stub
    return null;
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
      contentTypeScanner.next("\\s?boundary=\"?(.*)\"?");
      MatchResult result = contentTypeScanner.match();
      if (result.groupCount() == 1) {
        return result.group(1);
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
  
  /*String encoding = mimeHeaders.get(BatchRequestConstants.HTTP_CONTENT_TRANSFER_ENCODING);
  if(BatchRequestConstants.BINARY_ENCODING.equals(encoding)){
    
  }*/
}
