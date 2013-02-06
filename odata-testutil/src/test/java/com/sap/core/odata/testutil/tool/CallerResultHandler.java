package com.sap.core.odata.testutil.tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CallerResultHandler {

  private static final String REQUEST_METHOD = "REQUEST_METHOD";

  private static final Logger LOG = LoggerFactory.getLogger(CallerResultHandler.class);

  private static final String NULL_VALUE = "_NULL_";
  private static final String SEPARATOR = "|";
  private static final String HEADER_SEPARATOR = "||";
  private static final String VALUE_QUOTE = " ";

  private final StringBuilder errorLines = new StringBuilder();

  public CallerResultHandler() {
    
  }

  private String createJiraHeader() {
    if(testPath2TestResult.isEmpty()) {
      return "";
    }

    StringBuilder b = new StringBuilder();
    b.append(HEADER_SEPARATOR).append(" URI ").append(HEADER_SEPARATOR).append(" AcceptHeader ");
    Set<TestResult> results = getRepresentingTestResultSet();
    for (TestResult result: results) {
      String knownHostName = getKnownHostName(result.getBaseUri().getHost());
      b.append(HEADER_SEPARATOR).append(" ContentType (")
        .append(knownHostName).append(") ")
        .append(HEADER_SEPARATOR).append(" ResponseStatus (")
        .append(knownHostName).append(") ");
    }
    b.append(HEADER_SEPARATOR).append(" \n");    
    return b.toString();
  }

  
  
  private Set<TestResult> getRepresentingTestResultSet() {
    Set<TestResult> results = null;
    
    Set<Entry<TestPath, Set<TestResult>>> entries = testPath2TestResult.entrySet();
    for (Entry<TestPath, Set<TestResult>> entry : entries) {
      if(results == null || entry.getValue().size() > results.size()) {
        results = entry.getValue();
      }
    }
    return results;
  }

  private Map<TestPath, Set<TestResult>> testPath2TestResult = new HashMap<TestPath, Set<TestResult>>();
  
  public void handle(URI baseUri, TestPath testPath, HttpRequest request, HttpResponse response) {
    TestResult tr = new TestResult(baseUri, testPath.getPath());
    
    tr.addRequestHeaders(request.getAllHeaders());
    tr.addResponseHeaders(response.getAllHeaders());
    tr.addSomeValue(REQUEST_METHOD, request.getRequestLine().getMethod());
    tr.addSomeValue("RESPONSE_STATUS_CODE", String.valueOf(response.getStatusLine().getStatusCode()));
    
//    String key = testPath.getId();
    Set<TestResult> results = testPath2TestResult.get(testPath);
    if(results == null) {
      results = new TreeSet<TestResult>();
      testPath2TestResult.put(testPath, results);
    }
    results.add(tr);
  }

  public String getJiraResult() {
    StringBuilder b = new StringBuilder();
    
    b.append(createJiraHeader());
    // validate results
//    Set<Entry<TestPath, Set<TestResult>>> entries = testPath2TestResult.entrySet();
//    for (Entry<TestPath, Set<TestResult>> entry: entries) {
//      String line = createLineForJiraTable(entry.getValue());
//      b.append(line);
//    }
    //
    List<TestPath> testPaths = new ArrayList<TestPath>(testPath2TestResult.keySet());
    Collections.sort(testPaths);
    for (TestPath testPath : testPaths) {
      String line = createLineForJiraTable(testPath2TestResult.get(testPath));
      b.append(line);
    }
    //
    return b.toString();
  }

  public void writeJiraResultToFile(File file) {
    // this
    String line = getJiraResult();
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
  
  public void writeJiraResultToFile() {
    String timestamp = String.valueOf(System.currentTimeMillis());
    File file = new File("./caller_" + timestamp + ".jira");
    try {
      file.createNewFile();
      LOG.debug("Created jira file at path '" + file.getPath() + "'.");
    } catch (IOException e) {
      LOG.error("Unable to create file at path '" + file.getPath() + "'.");
    }

    writeJiraResultToFile(file);
  }
  
  private String createLineForJiraTable(Set<TestResult> results) {
    StringBuilder b = new StringBuilder(SEPARATOR);

    append(b, buildUriForJira(results));
    
    boolean acceptHeaderUnwritten = true;
    for (TestResult result: results) {
      if(acceptHeaderUnwritten) {
        append(b, result.getRequestHeader(HttpHeaders.ACCEPT));
        acceptHeaderUnwritten = false;
      }
      append(b, result.getResponseHeader(HttpHeaders.CONTENT_TYPE));
      append(b, result.getSomeValue("RESPONSE_STATUS_CODE"));
    }

    b.append("\n");

    return b.toString();
  }
  
  private String buildUriForJira(Set<TestResult> results) {
    
    // GET for 'path' on [ABAP GMD|alskdfj] [JAVA NWC| lsakfj] [ukn|alskdjf]
    StringBuilder b = new StringBuilder();

    for (TestResult result: results) {
      if(b.length() == 0) {
        String requestMethod = result.getSomeValue(REQUEST_METHOD);
        String testPath = result.getPath();
        b.append("_").append(requestMethod).append("_ for \"*").append(testPath).append("*\" on ");
      } else {
        b.append(", ");
      }
      String baseUri = result.getBaseUri().toString();
      String knownHostName = getKnownHostName(baseUri);
      b.append("[").append(knownHostName).append("|").append(baseUri).append(result.getPath()).append("]");
    }
    return b.toString();
  }

  private String getKnownHostName(String baseUri) {
    String lcBaseUri = baseUri.toLowerCase(Locale.ENGLISH);
    if(lcBaseUri.contains("localhost")) {
      return "localhost";
    } else if(lcBaseUri.contains("ldcigmd.wdf.sap.corp")) {
      return "ABAP GMD";
    } else if(lcBaseUri.contains("jpaas.sapbydesign.com")) {
      return "Java NWC";
    }
    return baseUri;
  }

  private void append(StringBuilder b, String value) {
    if(value == null) {
      value = NULL_VALUE;
    }
    b.append(VALUE_QUOTE).append(value).append(VALUE_QUOTE).append(SEPARATOR);    
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
    return getJiraResult();
  }
  
  public String getErrors() {
    return errorLines.toString();
  }
}
