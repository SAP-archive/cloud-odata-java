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
package com.sap.core.odata.api.processor;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.PathInfo;

/**
 * 
 * @author SAP AG
 */
public abstract class ODataRequest {

  public abstract String getRequestHeaderValue(String name);

  public abstract Map<String, List<String>> getRequestHeaders();

  public abstract InputStream getBody();

  public abstract PathInfo getPathInfo();

  public abstract ODataHttpMethod getMethod();

  public abstract List<Locale> getAcceptableLanguages();

  public abstract String getContentType();

  public abstract List<String> getAcceptHeaders();

  public abstract Map<String, String> getQueryParameters();

  public static ODataRequestBuilder requestHeaders(final Map<String, List<String>> headers) {
    return newBuilder().requestHeaders(headers);
  }

  public static ODataRequestBuilder body(final InputStream body) {
    return newBuilder().body(body);
  }

  public static ODataRequestBuilder pathInfo(final PathInfo pathInfo) {
    return newBuilder().pathInfo(pathInfo);
  }

  public static ODataRequestBuilder method(final ODataHttpMethod method) {
    return newBuilder().method(method);
  }

  public static ODataRequestBuilder acceptableLanguages(final List<Locale> acceptableLanguages) {
    return newBuilder().acceptableLanguages(acceptableLanguages);
  }

  public static ODataRequestBuilder contentType(final String contentType) {
    return newBuilder().contentType(contentType);
  }

  public static ODataRequestBuilder acceptHeaders(final List<String> acceptHeaders) {
    return newBuilder().acceptHeaders(acceptHeaders);
  }

  public static ODataRequestBuilder queryParameters(final Map<String, String> queryParameters) {
    return newBuilder().queryParameters(queryParameters);
  }

  public static ODataRequestBuilder fromRequest(final ODataRequest request) {
    return newBuilder().fromRequest(request);
  }

  /**
   * @return returns a new builder object
   */
  public static ODataRequestBuilder newBuilder() {
    return ODataRequestBuilder.newInstance();
  }

  public static abstract class ODataRequestBuilder {

    protected ODataRequestBuilder() {}

    private static ODataRequestBuilder newInstance() {
      return RuntimeDelegate.createODataRequestBuilder();
    }

    public abstract ODataRequest build();

    public abstract ODataRequestBuilder requestHeaders(Map<String, List<String>> headers);

    public abstract ODataRequestBuilder body(InputStream body);

    public abstract ODataRequestBuilder pathInfo(PathInfo pathInfo);

    public abstract ODataRequestBuilder method(ODataHttpMethod method);

    public abstract ODataRequestBuilder acceptableLanguages(List<Locale> acceptableLanguages);

    public abstract ODataRequestBuilder contentType(String contentType);

    public abstract ODataRequestBuilder acceptHeaders(List<String> acceptHeaders);

    public abstract ODataRequestBuilder queryParameters(Map<String, String> queryParameters);

    public abstract ODataRequestBuilder fromRequest(ODataRequest request);

  }

}
