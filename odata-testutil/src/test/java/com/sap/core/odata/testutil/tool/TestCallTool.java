package com.sap.core.odata.testutil.tool;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHeaders;

public class TestCallTool {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {

    List<String> paths = Arrays.asList("/", 
        "/Employess", // "/Managers", "/Rooms",
        "/Employess('1')",
        "/Employees('1')/$value",
        "/Employees('1')/Age/$value",
        "/Employees('1')/Location",
        "/$metadata"
        );
    
    String header = HttpHeaders.ACCEPT;
    List<String> headerValues = Arrays.asList("", 
        "text/plain", 
        "application/xml", 
        "application/json", 
        "application/atom+xml",
        "application/atomsvc+xml",
        "text/plain; charset=utf-8", 
        "application/xml; charset=utf-8", 
        "application/json; charset=utf-8", 
        "application/atom+xml; charset=utf-8",
        "application/atomsvc+xml; charset=utf-8"
        );
    List<TestPath> testPaths = TestPath.createTestPaths(paths, header, headerValues);
    // for a reduced test set
//    List<TestPath> testPaths = TestPath.createTestPaths(Arrays.asList("/", "/Employees"), 
//        header, Arrays.asList("", "application/xml"));

    CallerResultHandler handler = new CallerResultHandler();

    String localBaseUrl = "http://localhost:8080/com.sap.core.odata.ref.web/ReferenceScenario.svc";
    CallerConfig localConfig = new CallerConfig(localBaseUrl, handler, testPaths);

//    String nwcBaseUrl = "https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc";
//    CallerConfig nwcConfig = new CallerConfig(nwcBaseUrl, handler, testPaths).setProxy("proxy:8080");

//    String gmdUrl = "http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata";
//    CallerConfig gmdConfig = new CallerConfig(gmdUrl, handler, testPaths).setBasicAuthCredentials("user:pwd");

    AcceptHeaderCaller ahc = new AcceptHeaderCaller(localConfig);
    ahc.call();

    String result = handler.getResult();
    System.out.println(result);
  }
}
