/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.testutil.tool.core;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;

public class TestResult implements Comparable<TestResult> {

  public static final String REQUEST_METHOD = "REQUEST_METHOD";
  public static final String RESPONSE_STATUS_CODE = "RESPONSE_STATUS_CODE";
  public static final String RESPONSE_ENTITY_CONTENT = "RESPONSE_ENTITY_CONTENT";

  private final URI baseUri;
  private final String path;

  private final Map<String, String> reqHeaders = new HashMap<String, String>();
  private final Map<String, String> resHeaders = new HashMap<String, String>();
  private final Map<String, String> someValues = new HashMap<String, String>();

  public TestResult(final URI baseUri, final String path) {
    this.baseUri = baseUri;
    this.path = path;
  }

  public URI getBaseUri() {
    return baseUri;
  }

  public String getPath() {
    return path;
  }

  public void addSomeValue(final String key, final String value) {
    someValues.put(key, value);
  }

  public String getSomeValue(final String key) {
    return someValues.get(key);
  }

  public String getRequestHeader(final String name) {
    return reqHeaders.get(normalizeHeaderName(name));
  }

  public String getResponseHeader(final String name) {
    return getResponseHeader(name, false);
  }

  public String getResponseHeader(final String name, final boolean normalized) {
    String value = resHeaders.get(normalizeHeaderName(name));
    if (normalized) {
      return normalizeHeaderValue(value);
    }
    return value;
  }

  public void addRequestHeader(final String name, final String value) {
    reqHeaders.put(normalizeHeaderName(name), value);
  }

  public void addResponseHeader(final String name, final String value) {
    resHeaders.put(normalizeHeaderName(name), value);
  }

  public void addRequestHeaders(final Header[] allHeaders) {
    for (final Header header : allHeaders) {
      addRequestHeader(header.getName(), header.getValue());
    }
  }

  public void addResponseHeaders(final Header[] allHeaders) {
    for (final Header header : allHeaders) {
      addResponseHeader(header.getName(), header.getValue());
    }
  }

  private String normalizeHeaderName(final String name) {
    if (name == null) {
      throw new IllegalArgumentException("NULL header names are not allowed.");
    }
    return name.toLowerCase(Locale.ENGLISH);
  }

  private String normalizeHeaderValue(final String value) {
    if (value == null) {
      return null;
    }
    return value.replaceAll("\\s", "").toLowerCase(Locale.ENGLISH);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TestResult [\n\tbaseUri=" + baseUri +
        ", \n\tpath=" + path +
        ", \n\trequestHeaders=" + reqHeaders +
        ", \n\tresponseHeaders=" + resHeaders +
        ", \n\tvalues=" + someValues + "]";
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((baseUri == null) ? 0 : baseUri.hashCode());
    result = (prime * result) + ((path == null) ? 0 : path.hashCode());
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
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
    final TestResult other = (TestResult) obj;
    if (baseUri == null) {
      if (other.baseUri != null) {
        return false;
      }
    } else if (!baseUri.equals(other.baseUri)) {
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
  public int compareTo(final TestResult o) {
    if (baseUri == null) {
      return -1;
    } else if ((o == null) || (o.baseUri == null)) {
      return 1;
    }
    return baseUri.compareTo(o.baseUri);
  }
}
