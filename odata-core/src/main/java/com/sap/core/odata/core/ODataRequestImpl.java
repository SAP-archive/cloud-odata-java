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
 * @author SAP AG
 */
public class ODataRequestImpl implements ODataRequest {

  private ODataHttpMethod method;
  private Map<String, String> headers = new HashMap<String, String>();
  private Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
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

  @Deprecated
  public void setHeaders(final Map<String, String> headers) {
    this.headers = headers;
  }

  public void setRequestHeaders(final Map<String, List<String>> requestHeaders) {
    this.requestHeaders = requestHeaders;
  }

  public void setBody(final InputStream body) {
    this.body = body;
  }

  public void setPathInfo(final PathInfo pathInfo) {
    this.pathInfo = pathInfo;
  }

  @Override
  @Deprecated
  public String getHeaderValue(final String name) {
    return headers.get(name);
  }

  @Override
  @Deprecated
  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }

  @Override
  public String getRequestHeaderValue(final String name) {
    final List<String> headerList = requestHeaders.get(name);
    return headerList == null || headerList.isEmpty() ? null : headerList.get(0);
  }

  @Override
  public Map<String, List<String>> getRequestHeaders() {
    return Collections.unmodifiableMap(requestHeaders);
  }

  @Override
  public InputStream getBody() {
    return body;
  }

  @Override
  public ODataHttpMethod getMethod() {
    return method;
  }

  @Override
  public PathInfo getPathInfo() {
    return pathInfo;
  }

  public void setQueryParameters(final Map<String, String> queryParameters) {
    this.queryParameters = queryParameters;
  }

  public void setAcceptHeaders(final List<String> acceptHeaders) {
    this.acceptHeaders = acceptHeaders;
  }

  public void setContentType(final ContentType contentType) {
    this.contentType = contentType;
  }

  public void setAcceptableLanguages(final List<Locale> acceptableLanguages) {
    this.acceptableLanguages = acceptableLanguages;
  }
}
