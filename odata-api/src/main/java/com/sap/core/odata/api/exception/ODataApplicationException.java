package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

public class ODataApplicationException extends ODataException {

  private final HttpStatusCodes httpStatus;
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public ODataApplicationException(String message) {
    this(message, HttpStatusCodes.INTERNAL_SERVER_ERROR);
  }

  public ODataApplicationException(String message, HttpStatusCodes status) {
    super(message);
    this.httpStatus = status;
  }

  public ODataApplicationException(Throwable e) {
    this(null, e);
  }

  public ODataApplicationException(String message, Throwable e) {
    super(message, e);
    this.httpStatus = HttpStatusCodes.INTERNAL_SERVER_ERROR;
  }

  public ODataApplicationException(HttpStatusCodes status) {
    this(null, status);
  }

  public HttpStatusCodes getHttpStatus() {
    return this.httpStatus;
  }
}
