package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public abstract class ODataHttpException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  protected final HttpStatus httpStatus;

  public ODataHttpException(MessageReference messageReference, HttpStatus httpStatus) {
    this(messageReference, null, httpStatus);
  }

  public ODataHttpException(MessageReference messageReference, Throwable cause, HttpStatus httpStatus) {
    super(messageReference, cause);
    this.httpStatus = httpStatus;
  }

  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }
}
