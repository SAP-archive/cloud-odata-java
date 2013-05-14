package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;

/**
 * 
 * @author SAP AG
 */
public interface ODataRequest {

  String getHeaderValue(String name);
  
  Map<String, String> getHeaders();
  
  InputStream getBody();
  
  URI getUri();
  
  ODataHttpMethod getMethod();
  
}
