package com.sap.core.odata.core;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * 
 * @author SAP AG
 */
public class ODataRequestImpl implements ODataRequest {

  private ODataHttpMethod method;

  private Map<String, String> headers = new HashMap<String, String>();

  private InputStream body;

  private PathInfo pathInfo;

  public void setMethod(final ODataHttpMethod method) {
    this.method = method;
  }

  public void setHeaders(final Map<String, String> headers) {
    this.headers = headers;
  }

  public void setBody(final InputStream body) {
    this.body = body;
  }

  public void setPathInfo(final PathInfo pathInfo) {
    this.pathInfo = pathInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHeaderValue(final String name) {
    return headers.get(name);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InputStream getBody() {
    return body;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataHttpMethod getMethod() {
    return method;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PathInfo getPathInfo() {
    return pathInfo;
  }

}
