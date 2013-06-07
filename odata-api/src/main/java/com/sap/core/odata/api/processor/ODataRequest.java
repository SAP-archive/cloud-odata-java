package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * 
 * @author SAP AG
 */
public interface ODataRequest {

  @Deprecated
  String getHeaderValue(String name);

  @Deprecated
  Map<String, String> getHeaders();

  String getRequestHeaderValue(String name);
  
  Map<String, List<String>> getRequestHeaders();

  InputStream getBody();

  PathInfo getPathInfo();

  ODataHttpMethod getMethod();

  List<Locale> getAcceptableLanguages();

  String getContentType();

  List<String> getAcceptHeaders();

  Map<String, String> getQueryParameters();


}
