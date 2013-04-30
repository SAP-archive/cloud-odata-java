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

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;

public class ODataErrorContext {

  private String contentType;
  private HttpStatusCodes httpStatus;
  private String errorCode;
  private String message;
  private Locale locale;
  private Exception exception;
  private Map<String, List<String>> requestHeaders;
  private URI requestUri;

  public ODataErrorContext() {
    requestHeaders = new HashMap<String, List<String>>();
  }

  public Exception getException() {
    return exception;
  }

  public void setException(final Exception exception) {
    this.exception = exception;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public HttpStatusCodes getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(final HttpStatusCodes status) {
    httpStatus = status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(final String errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(final Locale locale) {
    this.locale = locale;
  }

  public void putRequestHeader(final String key, final List<String> value) {
    requestHeaders.put(key, value);
  }

  public Map<String, List<String>> getRequestHeaders() {
    return Collections.unmodifiableMap(requestHeaders);
  }

  public List<String> getRequestHeader(final String name) {
    return requestHeaders.get(name);
  }

  public void setRequestUri(final URI requestUri) {
    this.requestUri = requestUri;
  }

  public URI getRequestUri() {
    return requestUri;
  }
}
