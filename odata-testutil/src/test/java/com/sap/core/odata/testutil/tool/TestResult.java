package com.sap.core.odata.testutil.tool;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

public class TestResult implements Comparable<TestResult> {

  private final URI baseUri;
  private final String path;
  
  private Map<String, String> reqHeaders = new HashMap<String, String>();
  private Map<String, String> resHeaders = new HashMap<String, String>();
  private Map<String, String> someValues = new HashMap<String, String>();
  
  public TestResult(URI baseUri, String path) {
    this.baseUri = baseUri;
    this.path = path;
  }
  
  public URI getBaseUri() {
    return baseUri;
  }
  
  public String getPath() {
    return path;
  }

  public void addSomeValue(String key, String value) {
    someValues.put(key, value);
  }

  public String getSomeValue(String key) {
    return someValues.get(key);
  }

  public String getRequestHeader(String name) {
    return reqHeaders.get(name);
  }

  public String getResponseHeader(String name) {
    return resHeaders.get(name);
  }
  
  public void addRequestHeader(String name, String value) {
    reqHeaders.put(name, value);
  }

  public void addResponseHeader(String name, String value) {
    resHeaders.put(name, value);
  }

  public void addRequestHeaders(Header[] allHeaders) {
    for (Header header: allHeaders) {
      addRequestHeader(header.getName(), header.getValue());
    }
  }

  public void addResponseHeaders(Header[] allHeaders) {
    for (Header header: allHeaders) {
      addResponseHeader(header.getName(), header.getValue());
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((baseUri == null) ? 0 : baseUri.hashCode());
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
  }


  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TestResult other = (TestResult) obj;
    if (baseUri == null) {
      if (other.baseUri != null)
        return false;
    } else if (!baseUri.equals(other.baseUri))
      return false;
    if (path == null) {
      if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
      return false;
    return true;
  }

  @Override
  public int compareTo(TestResult o) {
    if(baseUri == null) {
      return -1;
    } else if(o == null || o.baseUri == null) {
      return 1;
    }
    return baseUri.compareTo(o.baseUri);
  }
}
