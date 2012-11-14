package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

public abstract class ODataHttpException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  protected final HttpStatusCodes httpStatus;

  public ODataHttpException(MessageReference messageReference, HttpStatusCodes httpStatus) {
    this(messageReference, null, httpStatus);
  }

  public ODataHttpException(MessageReference messageReference, Throwable cause, HttpStatusCodes httpStatus) {
    super(messageReference, cause);
    this.httpStatus = httpStatus;
  }

  public HttpStatusCodes getHttpStatus() {
    return this.httpStatus;
  }
}
