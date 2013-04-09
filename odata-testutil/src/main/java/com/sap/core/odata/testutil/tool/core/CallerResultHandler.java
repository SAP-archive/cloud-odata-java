package com.sap.core.odata.testutil.tool.core;

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

/**
 * @author SAP AG
 */
public class CallerResultHandler {

  public static final TestResultFilter USE_ALL_FILTER = new TestResultFilter() {
    @Override
    public boolean filterResults(Set<TestResult> results) {
      return true;
    }
  };
  
  public static final TestResultFilter USE_DIFF_FILTER = new TestResultFilter() {
    @Override
    public boolean filterResults(Set<TestResult> results) {
      if(results.size() > 1) {
        TestResult last = null;
        for (TestResult testResult : results) {
          if(last != null && testResult.compareTo(last) != 0) {
            String lastStatusCode = last.getSomeValue(RESPONSE_STATUS_CODE);
            String currentStatusCode = testResult.getSomeValue(RESPONSE_STATUS_CODE);
            String lastResponseHeader = last.getResponseHeader(HttpHeaders.CONTENT_TYPE, true);
            String currentResponseHeader = testResult.getResponseHeader(HttpHeaders.CONTENT_TYPE, true);

            if(isDifferent(lastStatusCode, currentStatusCode)
                || isDifferent(lastResponseHeader, currentResponseHeader)) {
              return true;
            }
          }
          last = testResult;
        }
      }
      return false;
    }
    
    private boolean isDifferent(String first, String second) {
      if(first == null && second == null) {
        return false;
      } else if(first == null) {
        return true;
      } else if(first.equals(second)) {
        return false;
      }
      return true;
    }
  };

  public static final String RESPONSE_STATUS_CODE = "RESPONSE_STATUS_CODE";
  public static final String REQUEST_METHOD = "REQUEST_METHOD";

  private static final Logger LOG = LoggerFactory.getLogger(CallerResultHandler.class);

  private static final String NULL_VALUE = "_NULL_";
  private static final String SEPARATOR = "|";
  private static final String HEADER_SEPARATOR = "||";
  private static final String VALUE_QUOTE = " ";

  private final StringBuilder errorLines = new StringBuilder();

  public CallerResultHandler() {

  }

  private String createJiraHeader() {
    if (testPath2TestResult.isEmpty()) {
      return "";
    }

    final StringBuilder b = new StringBuilder();
    b.append(HEADER_SEPARATOR).append(" URI ").append(HEADER_SEPARATOR).append(" AcceptHeader ");
    final Set<TestResult> results = getRepresentingTestResultSet();
    for (final TestResult result : results) {
      final String knownHostName = getKnownHostName(result.getBaseUri().getHost());
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

    final Set<Entry<TestPath, Set<TestResult>>> entries = testPath2TestResult.entrySet();
    for (final Entry<TestPath, Set<TestResult>> entry : entries) {
      if ((results == null) || (entry.getValue().size() > results.size())) {
        results = entry.getValue();
      }
    }
    return results;
  }

  private final Map<TestPath, Set<TestResult>> testPath2TestResult = new HashMap<TestPath, Set<TestResult>>();

  public void handle(final URI baseUri, final TestPath testPath, final HttpRequest request, final HttpResponse response) {
    final TestResult tr = new TestResult(baseUri, testPath.getPath());

    tr.addRequestHeaders(request.getAllHeaders());
    tr.addResponseHeaders(response.getAllHeaders());
    tr.addSomeValue(REQUEST_METHOD, request.getRequestLine().getMethod());
    tr.addSomeValue(RESPONSE_STATUS_CODE, String.valueOf(response.getStatusLine().getStatusCode()));

    //    String key = testPath.getId();
    Set<TestResult> results = testPath2TestResult.get(testPath);
    if (results == null) {
      results = new TreeSet<TestResult>();
      testPath2TestResult.put(testPath, results);
    }
    results.add(tr);
  }

  public String getJiraResult() {
    return getJiraResult(USE_ALL_FILTER);
  }
  
  public String getJiraResult(TestResultFilter filter) {
    final StringBuilder b = new StringBuilder();

    b.append(createJiraHeader());
    final List<TestPath> testPaths = new ArrayList<TestPath>(testPath2TestResult.keySet());
    Collections.sort(testPaths);
    for (final TestPath testPath : testPaths) {
      Set<TestResult> results = testPath2TestResult.get(testPath);
      if(filter.filterResults(results)) {
        final String line = createLineForJiraTable(results);
        b.append(line);
      }
    }
    //
    return b.toString();
  }

  public void writeJiraResultToFile(final File file) {
    // this
    final String line = getJiraResult();
    // to file
    FileWriter fw = null;
    try {
      fw = new FileWriter(file, true);
      fw.write(line);
    } catch (final IOException e) {
      e.printStackTrace();
    } finally {
      if (fw != null) {
        try {
          fw.close();
        } catch (final IOException e) {
          LOG.error("Unable to close filewriter for file at path '" + file.getPath() + "' because of exception '" + e.getMessage() + "'.");
        }
      }
    }
  }

  public void writeJiraResultToFile() {
    final String timestamp = String.valueOf(System.currentTimeMillis());
    final File file = new File("./caller_" + timestamp + ".jira");
    try {
      file.createNewFile();
      LOG.debug("Created jira file at path '" + file.getPath() + "'.");
    } catch (final IOException e) {
      LOG.error("Unable to create file at path '" + file.getPath() + "'.");
    }

    writeJiraResultToFile(file);
  }

  private String createLineForJiraTable(final Set<TestResult> results) {
    final StringBuilder b = new StringBuilder(SEPARATOR);

    append(b, buildUriForJira(results));

    boolean acceptHeaderUnwritten = true;
    for (final TestResult result : results) {
      if (acceptHeaderUnwritten) {
        append(b, result.getRequestHeader(HttpHeaders.ACCEPT));
        acceptHeaderUnwritten = false;
      }
      append(b, result.getResponseHeader(HttpHeaders.CONTENT_TYPE), getResultColor(results, true, HttpHeaders.CONTENT_TYPE));
      append(b, result.getSomeValue(RESPONSE_STATUS_CODE), getResultColor(results, false, RESPONSE_STATUS_CODE));
    }

    b.append("\n");

    return b.toString();
  }

  private JiraColor getResultColor(final Set<TestResult> results, final boolean isResponseHeader, final String key) {
    if (results == null || results.isEmpty() || results.size() == 1) {
      return JiraColor.NONE;
    } else {
      String tempValue = null;
      boolean first = true;
      for (TestResult testResult : results) {
        String value;
        if (isResponseHeader) {
          value = testResult.getResponseHeader(key);
          // XXX: this should not be necessary (but is currently because of whitespaces in content type)
          value = normalizeHeaderValue(value);
        } else {
          value = testResult.getSomeValue(key);
        }

        if (first) {
          tempValue = value;
          first = false;
        } else if (tempValue != null) {
          if (!tempValue.equals(value)) {
            return JiraColor.RED;
          }
        } else if (value != null) {
          return JiraColor.RED;
        }
      }
      return JiraColor.NONE;
    }
  }

  private String normalizeHeaderValue(final String value) {
    if (value == null) {
      return null;
    }
    return value.replaceAll("\\s", "");
  }

  private String buildUriForJira(final Set<TestResult> results) {

    // GET for 'path' on [ABAP GMD|alskdfj] [JAVA NWC| lsakfj] [ukn|alskdjf]
    final StringBuilder b = new StringBuilder();

    for (final TestResult result : results) {
      if (b.length() == 0) {
        final String requestMethod = result.getSomeValue(REQUEST_METHOD);
        final String testPath = result.getPath();
        b.append("_").append(requestMethod).append("_ for \"*").append(testPath).append("*\" on ");
      } else {
        b.append(", ");
      }
      final String baseUri = result.getBaseUri().toString();
      final String knownHostName = getKnownHostName(baseUri);
      b.append("[").append(knownHostName).append("|").append(baseUri).append(result.getPath()).append("]");
    }
    return b.toString();
  }

  private String getKnownHostName(final String baseUri) {
    final String lcBaseUri = baseUri.toLowerCase(Locale.ENGLISH);
    if (lcBaseUri.contains("localhost")) {
      return "localhost";
    } else if (lcBaseUri.contains("ldcigmd.wdf.sap.corp")) {
      return "ABAP GMD";
    } else if (lcBaseUri.contains("jpaas.sapbydesign.com")) {
      return "Java NWC";
    }
    return baseUri;
  }

  private void append(final StringBuilder b, final String value) {
    append(b, value, JiraColor.NONE);
  }

  private void append(final StringBuilder b, String value, final JiraColor color) {
    if (value == null) {
      value = NULL_VALUE;
    }

    if (JiraColor.NONE != color) {
      b.append("{color:").append(color.jiraCode).append("}");
    }
    b.append(VALUE_QUOTE).append(value).append(VALUE_QUOTE);
    if (JiraColor.NONE != color) {
      b.append("{color}");
    }
    b.append(SEPARATOR);
  }

  public void handle(final TestPath testPath, final HttpRequest request, final Exception e) {
    errorLines.append("\n--- START   ---")
        .append(testPath.toString())
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

  /**
   * 
   */
  enum JiraColor {
    NONE(""), RED("red");

    final String jiraCode;

    private JiraColor(final String code) {
      jiraCode = code;
    }
  }
}
