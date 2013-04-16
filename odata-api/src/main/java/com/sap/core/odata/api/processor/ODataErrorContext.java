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

import java.util.Locale;

import com.sap.core.odata.api.commons.HttpStatusCodes;

public class ODataErrorContext {

  private String contentType;
  private HttpStatusCodes httpStatus;
  private String errorCode;
  private String message;
  private Locale locale;
  private Exception exception;

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

}
