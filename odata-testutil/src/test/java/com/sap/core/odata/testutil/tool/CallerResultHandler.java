package com.sap.core.odata.testutil.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallerResultHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CallerResultHandler.class);

  private static final String NULL_VALUE = "_NULL_";
  private static final String SEPARATOR = "|";
  private static final String HEADER_SEPARATOR = "||";
  private static final String VALUE_QUOTE = " ";

  private final File file;
  private final StringBuilder lines;
  private final StringBuilder errorLines;

  public CallerResultHandler() {
    String timestamp = String.valueOf(System.currentTimeMillis());
    file = new File("./caller_" + timestamp + ".jira");
    try {
      file.createNewFile();
      LOG.debug("Created csv file at path '" + file.getPath() + "'.");
    } catch (IOException e) {
      LOG.error("Unable to create file at path '" + file.getPath() + "'.");
    }
    
    lines = new StringBuilder();
    errorLines = new StringBuilder();
    
    writeHeader();
  }

  private void writeHeader() {
    writeLine(HEADER_SEPARATOR + " URI " +
    		HEADER_SEPARATOR + " AcceptHeader " +
    		HEADER_SEPARATOR + " ContentType " +
    		HEADER_SEPARATOR + " ResponseStatus " +
    		HEADER_SEPARATOR + " \n");
  }

  public void handle(URI baseUri, TestPath testPath, HttpRequest request, HttpResponse response) {
    StringBuilder b = new StringBuilder(SEPARATOR);

    String uri = buildUriForJira(baseUri, testPath, request);
    append(b, uri);
    append(b, getHeaderValue(request, HttpHeaders.ACCEPT));
    append(b, getHeaderValue(response, HttpHeaders.CONTENT_TYPE));
    append(b, String.valueOf(response.getStatusLine().getStatusCode()));

    b.append("\n");

    writeLine(b.toString());
  }
  
  private String buildUriForJira(URI baseUri, TestPath testPath, HttpRequest request) {
    StringBuilder b = new StringBuilder();
    b.append("[_").append(request.getRequestLine().getMethod())
     .append("_ @ \"*").append(testPath.getPath()).append("*\"|")
     .append(baseUri.toString())
     .append(testPath.getPath())
     .append("]");
    return b.toString();
  }

  private void append(StringBuilder b, String value) {
    b.append(VALUE_QUOTE).append(value).append(VALUE_QUOTE).append(SEPARATOR);    
  }

  private void writeLine(String line) {
    // internal
    lines.append(line);
    // to file
    FileWriter fw = null;
    try {
      fw = new FileWriter(file, true);
      fw.write(line);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (IOException e) {
          LOG.error("Unable to close filewriter for file at path '" + file.getPath() + "' because of exception '" + e.getMessage() + "'.");
        }
      }
    }
  }

  /**
   * Get first header value or {@value #NULL_VALUE} if requested header was not set.
   * 
   * @param httpMessage
   * @param headerName
   * @return
   */
  private String getHeaderValue(HttpMessage httpMessage, String headerName) {
    Header header = httpMessage.getFirstHeader(headerName);
    if(header == null) {
      return NULL_VALUE;
    }
    return header.getValue();
  }

  public void handle(TestPath testPath, HttpRequest request, Exception e) {
    errorLines.append("\n--- START   ---")
          .append("--- Request ---")
          .append("\tURI: " + request.getRequestLine().getUri())
          .append("\tAcceptHeader: " + request.getFirstHeader(HttpHeaders.ACCEPT))
          .append("--- Exception ---")
          .append("\tException message: " + e.getMessage())
          .append("--- FINISHED ---\n");
  }
  
  public String getResult() {
    return lines.toString();
  }
  
  public String getErrors() {
    return errorLines.toString();
  }
}
