package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * 
 * @author SAP AG
 */
public interface ODataRequest {

  String getHeaderValue(String name);

  Map<String, String> getHeaders();

  InputStream getBody();

  PathInfo getPathInfo();

  ODataHttpMethod getMethod();

}
