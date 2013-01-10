package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.Set;

public abstract class ODataRequest {
  
  protected ODataRequest() {}
  
  /**
   * @param name http request header name
   * @return a header value or null if not set
   */
  public abstract String getHeader(String name);
  
  /**
   * @return content header value or null if not set
   */
  public abstract String getContentHeader();

  /**
   * @return a set of all available header names
   */
  public abstract Set<String> getHeaderNames();
  
  public abstract InputStream getContent();
}
