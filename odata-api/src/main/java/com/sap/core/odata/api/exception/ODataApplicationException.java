package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataApplicationException extends ODataException {

  private final HttpStatus httpStatus;
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ODataApplicationException(String message) {
    this(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public ODataApplicationException(String message, HttpStatus status) {
    super(message);
    this.httpStatus = status;
  }

  public ODataApplicationException(Throwable e) {
    this(null, e);
  }

  public ODataApplicationException(String message, Throwable e) {
    super(message, e);
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
  }

  public ODataApplicationException(HttpStatus status) {
    this(null, status);
  }

  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }
}
