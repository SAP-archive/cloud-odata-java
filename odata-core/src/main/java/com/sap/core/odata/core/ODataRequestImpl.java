/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
