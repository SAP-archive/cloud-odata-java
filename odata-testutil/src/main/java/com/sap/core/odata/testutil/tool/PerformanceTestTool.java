package com.sap.core.odata.testutil.tool;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.testutil.TestUtilRuntimeException;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.tool.core.CallerConfig;
import com.sap.core.odata.testutil.tool.core.SupaPerformanceTestClient;
import com.sap.core.odata.testutil.tool.core.TestGetRequest;
import com.sap.core.odata.testutil.tool.core.TestPostRequest;
import com.sap.core.odata.testutil.tool.core.TestRequest;

/**
 * Simple tool to define and process performance calls against an OData service and collect the result of these calls.
 * 
 * @author SAP AG
 */
public class PerformanceTestTool {
  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PerformanceTestTool.class);

  private static final String BASELINE_READ_XML_SUPA_CONFIG = "target/classes/performance/OData-Base-Read_XML-Perf-RefScenario-LocalLJS.properties";
  private static final String BASELINE_READ_JSON_SUPA_CONFIG = "target/classes/performance/OData-Base-Read_JSON-Perf-RefScenario-LocalLJS.properties";
  private static final String BASELINE_WRITE_SUPA_CONFIG = "target/classes/performance/OData-Base-Write-Perf-RefScenario-LocalLJS.properties";

  private Level consoleLogLevel;
  private Level fileLogLevel;
  private String supaHome;
  
  /**
   * simple main to start the tool
   * 
   * @param args not used
   * @throws IOException 
   */
  public static void main(final String[] args) throws IOException {
    PerformanceTestTool ptt = new PerformanceTestTool();
    ptt.start();
  }

  private void initializeProperties() throws IOException {
    Properties properties = getProperties();
    
    consoleLogLevel = Level.toLevel(properties.getProperty("performance.tool.console.log.level"));
    fileLogLevel = Level.toLevel(properties.getProperty("performance.tool.file.log.level"));
    supaHome = properties.getProperty("supa.home");
  }
  
  public void start() throws IOException {
    initializeProperties();
    
    activateLogging(consoleLogLevel, fileLogLevel);
    
    performanceRegressionBaseLineForReadXml(supaHome);
    performanceRegressionBaseLineForReadJson(supaHome);
    performanceRegressionBaseLineForWrite(supaHome);
  }

  /**
   * BaseLine for READ Performance tests.
   * Attention: Modifications here MUST to be matched with SUPA configuration {@value #BASELINE_READ_XML_SUPA_CONFIG}
   * 
   * @param supaHome path in which SUPA is installed
   */
  private void performanceRegressionBaseLineForReadXml(String supaHome) {
    final List<TestRequest> testRequests = new ArrayList<TestRequest>();

    testRequests.add(TestGetRequest.createGet("/Employee('1')", 10).addHeader("Accept", "application/atom+xml"));
    testRequests.add(TestGetRequest.createGet("/Employee('1')", 100).addHeader("Accept", "application/atom+xml"));
    testRequests.add(TestGetRequest.createGet("/Employee('1')", 1000).addHeader("Accept", "application/atom+xml"));

    testRequests.add(TestGetRequest.createGet("/Rooms", 10).addHeader("Accept", "application/atom+xml"));
    testRequests.add(TestGetRequest.createGet("/Rooms", 100).addHeader("Accept", "application/atom+xml"));
    testRequests.add(TestGetRequest.createGet("/Rooms", 1000).addHeader("Accept", "application/atom+xml"));

    runPerformanceRegressionFor(supaHome, BASELINE_READ_XML_SUPA_CONFIG, testRequests);
  }

  private void performanceRegressionBaseLineForReadJson(String supaHome) {
    final List<TestRequest> testRequests = new ArrayList<TestRequest>();

    testRequests.add(TestGetRequest.createGet("/Employee('1')", 10).addHeader("Accept", "application/json"));
    testRequests.add(TestGetRequest.createGet("/Employee('1')", 100).addHeader("Accept", "application/json"));
    testRequests.add(TestGetRequest.createGet("/Employee('1')", 1000).addHeader("Accept", "application/json"));

    testRequests.add(TestGetRequest.createGet("/Rooms", 10).addHeader("Accept", "application/json"));
    testRequests.add(TestGetRequest.createGet("/Rooms", 100).addHeader("Accept", "application/json"));
    testRequests.add(TestGetRequest.createGet("/Rooms", 1000).addHeader("Accept", "application/json"));

    runPerformanceRegressionFor(supaHome, BASELINE_READ_JSON_SUPA_CONFIG, testRequests);
  }

  /**
   * BaseLine for WRITE Performance tests.
   * Attention: Modifications here MUST to be matched with SUPA configuration {@value #BASELINE_WRITE_SUPA_CONFIG}
   * 
   * @param supaHome path in which SUPA is installed
   */
  private void performanceRegressionBaseLineForWrite(String supaHome) {
    final List<TestRequest> testRequests = new ArrayList<TestRequest>();
    String content = StringHelper.generateData(2048);
    testRequests.add(TestPostRequest.createPost("/Employees", 10, "application/json", content));
    testRequests.add(TestPostRequest.createPost("/Employees", 100, "application/json", content));
    testRequests.add(TestPostRequest.createPost("/Employees", 1000, "application/json", content));

    runPerformanceRegressionFor(supaHome, BASELINE_WRITE_SUPA_CONFIG, testRequests);
  }

  private void runPerformanceRegressionFor(String supaHome, String supaConfig, final List<TestRequest> testRequests) {
    try {
      final String localBaseUrl = "http://localhost:8080/com.sap.core.odata.ref.web/ReferenceScenario.svc";
      final CallerConfig localConfig = new CallerConfig(localBaseUrl, testRequests);

      SupaPerformanceTestClient perfTestClient = 
          SupaPerformanceTestClient.create(localConfig, "http://localhost:8181")
            .startSupa(supaHome, supaConfig)
            .exitSupa()
            .warmupRuns(2)
            .runsPerTest(3)
            .build();
      String path = perfTestClient.runMeasurement();
      
      LOG.info("Excel is here: " + path);

    } catch (Exception e) {
      throw new TestUtilRuntimeException(e);
    }
  }
  
  /**
   * Configure LOG4J logger which log the pure log message to console or file.
   * 
   * @param filename
   * @throws IOException
   */
  private void activateLogging(Level consoleLogLevel, Level fileLogLevel) throws IOException {
    Logger rootLogger = Logger.getRootLogger();
    rootLogger.getLoggerRepository().resetConfiguration();
    rootLogger.setLevel(Level.INFO);
    
    if (fileLogLevel != null && !Level.OFF.equals(fileLogLevel)) {
      String filename = getClass().getSimpleName() + "_" + new SimpleDateFormat("yyMMdd_HHmmss").format(new Date()) + ".txt";
      final Layout layout = new PatternLayout("%m\n");
      boolean append = true;
      final Appender fileAppender = new FileAppender(layout, filename, append);
      fileAppender.setName(filename);
      rootLogger.addAppender(fileAppender);
      rootLogger.setLevel(fileLogLevel);
    }
    
    if(consoleLogLevel != null && !Level.OFF.equals(consoleLogLevel)) {
      final Layout layout = new PatternLayout("%m\n");
      final Appender consoleAppender = new ConsoleAppender(layout);
      consoleAppender.setName(getClass().getPackage().getName());
      rootLogger.addAppender(consoleAppender);
      rootLogger.setLevel(consoleLogLevel);      
    }
  }
  
  private Properties getProperties() throws IOException {
    Properties p = new Properties();
    p.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("performance/performanceTestTool.properties"));
    return p;
  }

}
