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
