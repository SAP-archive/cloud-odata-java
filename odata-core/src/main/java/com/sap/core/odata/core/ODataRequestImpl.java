package com.sap.core.odata.core;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sap.core.odata.api.processor.ODataRequest;

public class ODataRequestImpl extends ODataRequest {

  private static final String MEDIA_TYPE = "MEDIA_TYPE";
  private final InputStream content;
  private final Map<String, String> headers;
  
  /**
   * 
   * @param content
   * @param mediaType <code>media type</code> value of content
   */
  private ODataRequestImpl(InputStream content, String mediaType) {
    this.content = content;
    this.headers = new HashMap<String, String>();
    
    this.headers.put(MEDIA_TYPE, mediaType);
  }
  
  @Override
  public String getHeader(String name) {
    return headers.get(name);
  }

  @Override
  public String getMediaType() {
    return headers.get(MEDIA_TYPE);
  }

  @Override
  public Set<String> getHeaderNames() {
    return Collections.unmodifiableSet(headers.keySet());
  }

  @Override
  public InputStream getContent() {
    return content;
  }
  
  public static ODataRequestBuilder create(InputStream contentBody, String mediaType) {
    return new ODataRequestBuilder(contentBody, mediaType);
  }

  public static class ODataRequestBuilder {
    private final ODataRequestImpl request;

    public ODataRequestBuilder(InputStream content, String mediaType) {
      request = new ODataRequestImpl(content, mediaType);
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
