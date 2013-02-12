package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * {@link ODataMessageException} with a HTTP status code.
 * @author SAP AG
 */
public abstract class ODataHttpException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  protected final HttpStatusCodes httpStatus;

  public static final MessageReference COMMON = createMessageReference(ODataHttpException.class, "COMMON");

  public ODataHttpException(MessageReference messageReference, HttpStatusCodes httpStatus) {
    this(messageReference, null, httpStatus);
  }

  public ODataHttpException(MessageReference messageReference, HttpStatusCodes httpStatus, String errorCode) {
    this(messageReference, null, httpStatus, errorCode);
  }

  public ODataHttpException(MessageReference messageReference, Throwable cause, HttpStatusCodes httpStatus) {
    this(messageReference, cause, httpStatus, null);
  }

  public ODataHttpException(MessageReference messageReference, Throwable cause, HttpStatusCodes httpStatus, String errorCode) {
    super(messageReference, cause, errorCode);
    this.httpStatus = httpStatus;
  }

  public HttpStatusCodes getHttpStatus() {
    return httpStatus;
  }
}
