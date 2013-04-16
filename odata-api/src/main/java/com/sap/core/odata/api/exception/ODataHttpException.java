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

  public ODataHttpException(final MessageReference messageReference, final HttpStatusCodes httpStatus) {
    this(messageReference, null, httpStatus);
  }

  public ODataHttpException(final MessageReference messageReference, final HttpStatusCodes httpStatus, final String errorCode) {
    this(messageReference, null, httpStatus, errorCode);
  }

  public ODataHttpException(final MessageReference messageReference, final Throwable cause, final HttpStatusCodes httpStatus) {
    this(messageReference, cause, httpStatus, null);
  }

  public ODataHttpException(final MessageReference messageReference, final Throwable cause, final HttpStatusCodes httpStatus, final String errorCode) {
    super(messageReference, cause, errorCode);
    this.httpStatus = httpStatus;
  }

  public HttpStatusCodes getHttpStatus() {
    return httpStatus;
  }
}
