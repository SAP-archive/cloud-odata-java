package com.sap.core.odata.core;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.processor.ODataRequest;

public class ODataRequestImpl extends ODataRequest {

  private final InputStream content;
  private final Map<String, String> headers;
  
  private ODataRequestImpl(InputStream content, String contentType) {
    this.content = content;
    this.headers = new HashMap<String, String>();
    
    this.headers.put(HttpHeaders.CONTENT_TYPE, contentType);
  }
  
  @Override
  public String getHeader(String name) {
    return headers.get(name);
  }

  @Override
  public String getContentHeader() {
    return headers.get(HttpHeaders.CONTENT_TYPE);
  }

  @Override
  public Set<String> getHeaderNames() {
    return Collections.unmodifiableSet(headers.keySet());
  }

  @Override
  public InputStream getContent() {
    return content;
  }
  
  public static ODataRequestBuilder create(InputStream contentBody, String contentType) {
    return new ODataRequestBuilder(contentBody, contentType);
  }

  public static class ODataRequestBuilder {
    private final ODataRequestImpl request;

    public ODataRequestBuilder(InputStream content, String contentType) {
      request = new ODataRequestImpl(content, contentType);
    }
    
    public ODataRequestBuilder addHeader(String name, String value) {
      request.headers.put(name, value);
      return this;
    }
    
    public ODataRequest build() {
      return request;
    }
  }
}
