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
package com.sap.core.odata.core;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * @author SAP AG
 */
public class ODataResponseImpl extends ODataResponse {

  private HttpStatusCodes status;
  private Object entity;
  private HashMap<String, String> headers;

  @Override
  public HttpStatusCodes getStatus() {
    return status;
  }

  @Override
  public Object getEntity() {
    return entity;
  }

  @Override
  public void close() throws IOException {
    if (entity != null && entity instanceof Closeable) {
      Closeable closeableEntity = (Closeable) entity;
      closeableEntity.close();
    }
  }

  @Override
  public String getHeader(final String name) {
    return headers.get(name);
  }

  @Override
  public Set<String> getHeaderNames() {
    return headers.keySet();
  }

  @Override
  public String getIdLiteral() {
    return headers.get(HttpHeaders.LOCATION);
  }

  @Override
  public String getETag() {
    return headers.get(HttpHeaders.ETAG);
  }

  @Override
  public String getContentHeader() {
    return headers.get(HttpHeaders.CONTENT_TYPE);
  }

  @Override
  public boolean containsHeader(final String header) {
    boolean contains = false;
    for (String containedHeader : headers.keySet()) {
      if (containedHeader.equalsIgnoreCase(header)) {
        contains = true;
        break;
      }
    }
    return contains;
  }

  public class ODataResponseBuilderImpl extends ODataResponseBuilder {
    private HttpStatusCodes status;
    private Object entity;
    private HashMap<String, String> headers = new HashMap<String, String>();

    @Override
    public ODataResponse build() {
      ODataResponseImpl.this.status = status;
      ODataResponseImpl.this.entity = entity;
      ODataResponseImpl.this.headers = headers;

      return ODataResponseImpl.this;
    }

    @Override
    public ODataResponseBuilder status(final HttpStatusCodes status) {
      this.status = status;
      return this;
    }

    @Override
    public ODataResponseBuilder entity(final Object entity) {
      this.entity = entity;
      return this;
    }

    @Override
    public ODataResponseBuilder header(final String name, final String value) {
      if (value == null) {
        headers.remove(name);
      } else {
        headers.put(name, value);
      }

      return this;
    }

    @Override
    public ODataResponseBuilder idLiteral(final String idLiteral) {
      return header(HttpHeaders.LOCATION, idLiteral);
    }

    @Override
    public ODataResponseBuilder eTag(final String eTag) {
      return header(HttpHeaders.ETAG, eTag);
    }

    @Override
    public ODataResponseBuilder contentHeader(final String value) {
      return header(HttpHeaders.CONTENT_TYPE, value);
    }

    @Override
    protected ODataResponseBuilder fromResponse(final ODataResponse response) {
      status = response.getStatus();
      entity = response.getEntity();

      headers = new HashMap<String, String>();
      for (String key : response.getHeaderNames()) {
        headers.put(key, response.getHeader(key));
      }

      return this;
    }

  }
}
