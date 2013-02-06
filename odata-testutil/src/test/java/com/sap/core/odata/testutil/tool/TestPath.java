package com.sap.core.odata.testutil.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;

public class TestPath {

  private String path;
  private final Map<String, String> headers;
  
  public TestPath(String path) {
    super();
    this.path = path;
    this.headers = new HashMap<String, String>();
  }
  
  public static TestPath create(String path) {
    return new TestPath(path);
  }
  
  public String getPath() {
    return path;
  }
  
  public TestPath setAcceptHeader(String value) {
    addHeader(HttpHeaders.ACCEPT, value);
    return this;
  }
  
  public TestPath addHeader(String name, String value) {
    headers.put(name, value);
    return this;
  }
  
  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }
  
  public void applyHeaders(HttpRequest request) {
    Set<Entry<String, String>> entries = headers.entrySet();
    
    for (Entry<String, String> entry : entries) {
      request.addHeader(entry.getKey(), entry.getValue());
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TestPath [path=" + path + ", headers=" + headers + "]";
  }

  //
  public static List<TestPath> createTestPaths(List<String> paths, String header, List<String> headerValues) {
    List<TestPath> testPaths = new ArrayList<TestPath>();
    
    for (String path : paths) {
      for (String value : headerValues) {
        TestPath tp = new TestPath(path);
        if(value != null && value.length() > 0) {
          tp.setAcceptHeader(value);
        }
        testPaths.add(tp);
      }
    }
    
    return testPaths;
  }

}
