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

import java.util.HashMap;
import java.util.Set;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;

public class ODataResponseImpl extends ODataResponse {

  private HttpStatusCodes status;
  private Object entity;
  private HashMap<String, String> header;
  private String idLiteral;
  private String eTag;

  @Override
  public HttpStatusCodes getStatus() {
    return status;
  }

  @Override
  public Object getEntity() {
    return entity;
  }

  @Override
  public String getHeader(final String name) {
    return header.get(name);
  }

  @Override
  public Set<String> getHeaderNames() {
    return header.keySet();
  }

  @Override
  public String getIdLiteral() {
    return idLiteral;
  }

  @Override
  public String getETag() {
    return eTag;
  }

  @Override
  public String getContentHeader() {
    return header.get(HttpHeaders.CONTENT_TYPE);
  }

  @Override
  public boolean containsHeader(final String header) {

    boolean contains = false;

    for (String containedHeader : this.header.keySet()) {
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
    private HashMap<String, String> header = new HashMap<String, String>();
    private String idLiteral;
    private String eTag;

    @Override
    public ODataResponse build() {
      ODataResponseImpl.this.entity = entity;
      ODataResponseImpl.this.header = header;
      ODataResponseImpl.this.eTag = eTag;
      ODataResponseImpl.this.status = status;
      ODataResponseImpl.this.idLiteral = idLiteral;

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
        header.remove(name);
      } else {
        header.put(name, value);
      }

      return this;
    }

    @Override
    public ODataResponseBuilder idLiteral(final String idLiteral) {
      this.idLiteral = idLiteral;
      return this;
    }

    @Override
    public ODataResponseBuilder eTag(final String eTag) {
      this.eTag = eTag;
      return this;
    }

    @Override
    public ODataResponseBuilder contentHeader(final String value) {
      header.put(HttpHeaders.CONTENT_TYPE, value);
      return this;
    }

    @Override
    protected ODataResponseBuilder fromResponse(final ODataResponse response) {
      entity = response.getEntity();
      eTag = response.getETag();
      idLiteral = response.getIdLiteral();

      header = new HashMap<String, String>();
      for (String key : response.getHeaderNames()) {
        header.put(key, response.getHeader(key));
      }

      return this;
    }

  }
}
