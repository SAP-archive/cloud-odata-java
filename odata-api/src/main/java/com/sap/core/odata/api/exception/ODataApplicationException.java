package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataApplicationException extends ODataException {

  private static final long serialVersionUID = 1L;
  private String errorCode = ODataApplicationException.class.getName();;
  private HttpStatusCodes httpStatus = HttpStatusCodes.INTERNAL_SERVER_ERROR;

  public ODataApplicationException(String message) {
    super(message);
  }

  public ODataApplicationException(String message, HttpStatusCodes status) {
    super(message);
    this.httpStatus = status;
  }

  public ODataApplicationException(String message, HttpStatusCodes status, String errorCode) {
    this(message, status);
    this.errorCode = errorCode;
  }

  public ODataApplicationException(String message, HttpStatusCodes status, String errorCode, Throwable e) {
    super(message, e);
    this.errorCode = errorCode;
    this.httpStatus = status;
  }

  public ODataApplicationException(Throwable e) {
    super(e);
  }

  public ODataApplicationException(String message, Throwable e) {
    super(message, e);
  }

  public ODataApplicationException(String message, HttpStatusCodes status, Throwable e) {
    this(message, e);
    this.httpStatus = status;
  }

  public ODataApplicationException(String message, String errorCode, Throwable e) {
    this(message, e);
    this.errorCode = errorCode;

  }

  public HttpStatusCodes getHttpStatus() {
    return this.httpStatus;
  }

  /**
   * Default code is "ODataApplicationException.class"
   * @return <b>String</b>The error code displayed in the error message. Mandatory after OData specification.
   */
  public String getCode() {
    return this.errorCode;
  }
}
