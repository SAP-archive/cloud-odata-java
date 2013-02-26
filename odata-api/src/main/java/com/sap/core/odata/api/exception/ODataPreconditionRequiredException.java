package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 428 precondition required
 * @author SAP AG
 */
public class ODataPreconditionRequiredException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataPreconditionRequiredException.class, "COMMON");

  public ODataPreconditionRequiredException(final MessageReference context) {
    super(context, HttpStatusCodes.PRECONDITION_REQUIRED);
  }

  public ODataPreconditionRequiredException(final MessageReference context, final Throwable cause) {
    super(context, cause, HttpStatusCodes.PRECONDITION_REQUIRED);
  }

  public ODataPreconditionRequiredException(final MessageReference context, final String errorCode) {
    super(context, HttpStatusCodes.PRECONDITION_REQUIRED, errorCode);
  }

  public ODataPreconditionRequiredException(final MessageReference context, final Throwable cause, final String errorCode) {
    super(context, cause, HttpStatusCodes.PRECONDITION_REQUIRED, errorCode);
  }
}
