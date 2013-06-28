package com.sap.core.odata.testutil.tool.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;

/**
 * @author SAP AG
 */
public abstract class TestRequest implements Comparable<TestRequest> {

  public enum RequestHttpMethod { GET, POST, PUT, DELETE };
  
  private final String path;
  private final Map<String, String> headers;
  private final int callCount;
  protected RequestHttpMethod requestHttpMethod;

  TestRequest(final String path, int callCount) {
    super();
    this.path = path;
    this.headers = new HashMap<String, String>();
    this.callCount = callCount;
    this.requestHttpMethod = RequestHttpMethod.GET;
  }

  public static TestRequest createGet(final String path) {
    return new TestGetRequest(path, 1);
  }

  public static TestRequest createGet(final String path, int callCount) {
    return new TestGetRequest(path, callCount);
  }

  public static TestRequest createPost(final String path, int callCount, String contentType, String content) {
    return new TestPostRequest(path, callCount, contentType, content);
  }

  public static List<TestRequest> createTestPaths(final List<String> paths, final List<String> headerValues) {
    final List<TestRequest> testPaths = new ArrayList<TestRequest>();

    for (final String path : paths) {
      for (final String value : headerValues) {
        final TestRequest testPath = new TestGetRequest(path, 1);
        if ((value != null) && (value.length() > 0)) {
          testPath.setAcceptHeader(value);
        }
        testPaths.add(testPath);
      }
    }

    return testPaths;
  }

  public void setHttpMethod(RequestHttpMethod requestHttpMethod) {
    this.requestHttpMethod = requestHttpMethod;
  }
  
  public RequestHttpMethod getHttpMethod() {
    return requestHttpMethod;
  }
  
  public int getCallCount() {
    return callCount;
  }

  public String getPath() {
    return path;
  }

  public TestRequest setAcceptHeader(final String value) {
    addHeader(HttpHeaders.ACCEPT, value);
    return this;
  }

  public TestRequest addHeader(final String name, final String value) {
    headers.put(name, value);
    return this;
  }

  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }

  public void applyHeaders(final HttpRequest request) {
    final Set<Entry<String, String>> entries = headers.entrySet();

    for (final Entry<String, String> entry : entries) {
      request.addHeader(entry.getKey(), entry.getValue());
    }
  }

  public String getId() {
    return String.valueOf(hashCode());
  }

  @Override
  public String toString() {
    return "TestPath [path=" + path + ", headers=" + headers + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((headers == null) ? 0 : headers.hashCode());
    result = (prime * result) + ((path == null) ? 0 : path.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final TestRequest other = (TestRequest) obj;
    if (headers == null) {
      if (other.headers != null) {
        return false;
      }
    } else if (!headers.equals(other.headers)) {
      return false;
    }
    if (path == null) {
      if (other.path != null) {
        return false;
      }
    } else if (!path.equals(other.path)) {
      return false;
    }
    return true;
  }

  @Override
  public int compareTo(final TestRequest o) {
    if (path == null) {
      return -1;
    } else if ((o == null) || (o.path == null)) {
      return 1;
    }
    return path.compareTo(o.path);
  }
}
