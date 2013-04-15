/**
 * (c) 2013 by SAP AG
 */
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
public class TestPath implements Comparable<TestPath> {

  private final String path;
  private final Map<String, String> headers;

  public TestPath(final String path) {
    super();
    this.path = path;
    headers = new HashMap<String, String>();
  }

  public static TestPath create(final String path) {
    return new TestPath(path);
  }

  public String getPath() {
    return path;
  }

  public TestPath setAcceptHeader(final String value) {
    addHeader(HttpHeaders.ACCEPT, value);
    return this;
  }

  public TestPath addHeader(final String name, final String value) {
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
    final TestPath other = (TestPath) obj;
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

  public static List<TestPath> createTestPaths(final List<String> paths, final List<String> headerValues) {
    final List<TestPath> testPaths = new ArrayList<TestPath>();

    for (final String path : paths) {
      for (final String value : headerValues) {
        final TestPath testPath = new TestPath(path);
        if ((value != null) && (value.length() > 0)) {
          testPath.setAcceptHeader(value);
        }
        testPaths.add(testPath);
      }
    }

    return testPaths;
  }

  @Override
  public int compareTo(final TestPath o) {
    if (path == null) {
      return -1;
    } else if ((o == null) || (o.path == null)) {
      return 1;
    }
    return path.compareTo(o.path);
  }
}
