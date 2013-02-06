package com.sap.core.odata.testutil.tool;

import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpHeaders;

public class TestCallTool {

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {

    final List<String> paths = Arrays.asList("/",
        "/Employess", // "/Managers", "/Rooms",
        "/Employess('1')",
        "/Employees('1')/$value",
        "/Employees('1')/Age/$value",
        "/Employees('1')/Location",
        "/$metadata"
        );

    final String header = HttpHeaders.ACCEPT;
    final List<String> headerValues = Arrays.asList("",
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
    final List<TestPath> testPaths = TestPath.createTestPaths(paths, header, headerValues);
    // for a reduced test set
    //    List<TestPath> testPaths = TestPath.createTestPaths(Arrays.asList("/", "/Employees"), 
    //        header, Arrays.asList("", "application/xml"));

    final CallerResultHandler handler = new CallerResultHandler();

    final String localBaseUrl = "http://localhost:8080/com.sap.core.odata.ref.web/ReferenceScenario.svc";
    final CallerConfig localConfig = new CallerConfig(localBaseUrl, handler, testPaths);
    AcceptHeaderCaller.create(localConfig).call();

    //    String nwcBaseUrl = "https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc";
    //    CallerConfig nwcConfig = new CallerConfig(nwcBaseUrl, handler, testPaths).setProxy("proxy:8080");
    //    AcceptHeaderCaller.create(nwcConfig).call();

    //    String gmdUrl = "http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata";
    //    CallerConfig gmdConfig = new CallerConfig(gmdUrl, handler, testPaths).setBasicAuthCredentials("user:pwd");
    //    AcceptHeaderCaller.create(gmdConfig).call();

    final String result = handler.getResult();
    System.out.println(result);
  }
}
