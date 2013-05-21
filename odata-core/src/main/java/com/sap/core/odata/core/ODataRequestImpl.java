package com.sap.core.odata.core;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.core.commons.ContentType;

/**
 * 
 * @author SAP AG
 */
public class ODataRequestImpl implements ODataRequest {

  private ODataHttpMethod method;
  private Map<String, String> headers = new HashMap<String, String>();
  private InputStream body;
  private PathInfo pathInfo;
  private Map<String, String> queryParameters;
  private List<String> acceptHeaders;
  private ContentType contentType;
  private List<Locale> acceptableLanguages;

  @Override
  public Map<String, String> getQueryParameters() {
    return queryParameters;
  }

  @Override
  public List<String> getAcceptHeaders() {
    return acceptHeaders;
  }

  @Override
  public String getContentType() {
    return contentType == null ? null : contentType.toContentTypeString();
  }

  @Override
  public List<Locale> getAcceptableLanguages() {
    return acceptableLanguages;
  }

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

  public void setQueryParameters(Map<String, String> queryParameters) {
    this.queryParameters = queryParameters;
  }

  public void setAcceptHeaders(List<String> acceptHeaders) {
    this.acceptHeaders = acceptHeaders;
  }

  public void setContentType(ContentType contentType) {
    this.contentType = contentType;
  }

  public void setAcceptableLanguages(List<Locale> acceptableLanguages) {
    this.acceptableLanguages = acceptableLanguages;
  }

}
