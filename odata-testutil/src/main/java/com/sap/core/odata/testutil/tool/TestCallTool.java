package com.sap.core.odata.testutil.tool;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.sap.core.odata.testutil.TestUtilRuntimeException;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.tool.core.CallerConfig;
import com.sap.core.odata.testutil.tool.core.SupaPerformanceTestClient;
import com.sap.core.odata.testutil.tool.core.TestGetRequest;
import com.sap.core.odata.testutil.tool.core.TestPostRequest;
import com.sap.core.odata.testutil.tool.core.TestRequest;

/**
 * Simple tool to define and process calls against an OData service and collect the result of these calls.
 * Currently this is only used and configured to do ContentNegotiation test calls for different URI-Types and collect/print the
 * result in JIRA compatible markup syntax.
 * @author SAP AG
 */
public class TestCallTool {
  private static final Logger LOG = Logger.getLogger(TestCallTool.class);

  static {
    BasicConfigurator.configure();
  }

  /**
   * simple main to start the tool
   * 
   * @param args not used
   */
  public static void main(final String[] args) {
    testCaseAdvancedPerformanceRegression();
  }

  
  private static void testCaseAdvancedPerformanceRegression() {
    try {
      final List<TestRequest> testRequests = new ArrayList<TestRequest>();
      String content = StringHelper.generateData(2048);
      testRequests.add(TestPostRequest.createPost("/Employees", 10, "application/json", content));
      testRequests.add(TestPostRequest.createPost("/Employees", 100, "application/json", content));
      testRequests.add(TestPostRequest.createPost("/Employees", 1000, "application/json", content));

      testRequests.add(TestGetRequest.createGet("/Employee('1')", 10).addHeader("Accept", "application/atom+xml"));
      testRequests.add(TestGetRequest.createGet("/Employee('1')", 100).addHeader("Accept", "application/atom+xml"));
      testRequests.add(TestGetRequest.createGet("/Employee('1')", 1000).addHeader("Accept", "application/atom+xml"));

      testRequests.add(TestGetRequest.createGet("/Employee('1')", 10).addHeader("Accept", "application/json"));
      testRequests.add(TestGetRequest.createGet("/Employee('1')", 100).addHeader("Accept", "application/json"));
      testRequests.add(TestGetRequest.createGet("/Employee('1')", 1000).addHeader("Accept", "application/json"));

      testRequests.add(TestGetRequest.createGet("/Rooms", 10).addHeader("Accept", "application/atom+xml"));
      testRequests.add(TestGetRequest.createGet("/Rooms", 100).addHeader("Accept", "application/atom+xml"));
      testRequests.add(TestGetRequest.createGet("/Rooms", 1000).addHeader("Accept", "application/atom+xml"));

      testRequests.add(TestGetRequest.createGet("/Rooms", 10).addHeader("Accept", "application/json"));
      testRequests.add(TestGetRequest.createGet("/Rooms", 100).addHeader("Accept", "application/json"));
      testRequests.add(TestGetRequest.createGet("/Rooms", 1000).addHeader("Accept", "application/json"));

      final String localBaseUrl = "http://localhost:8080/com.sap.core.odata.ref.web/ReferenceScenario.svc";
      final CallerConfig localConfig = new CallerConfig(localBaseUrl, testRequests);

      final String supaHome = "~/Performance/supa/";
      
      SupaPerformanceTestClient perfTestClient = 
          SupaPerformanceTestClient.create(localConfig, "http://localhost:8181")
            .startSupa(supaHome)
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
}
