package com.sap.core.odata.core;

import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;

/**
 * 
 * @author SAP AG
 */
public class ODataRequestImpl implements ODataRequest {

  private ODataHttpMethod method;

  private Map<String, String> headers = new HashMap<String, String>();

  private InputStream body;

  private URI uri;

  public void setMethod(ODataHttpMethod method) {
    this.method = method;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public void setBody(InputStream body) {
    this.body = body;
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getHeaderValue(String name) {
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
  public URI getUri() {
    return uri;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ODataHttpMethod getMethod() {
    return method;
  }

}
