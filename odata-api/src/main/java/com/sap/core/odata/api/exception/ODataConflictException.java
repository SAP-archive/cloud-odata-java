package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 409 Conflict
 * @author SAP AG
 */
public class ODataConflictException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataConflictException.class, "COMMON");

  public ODataConflictException(final MessageReference context) {
    super(context, HttpStatusCodes.CONFLICT);
  }

  public ODataConflictException(final MessageReference context, final Throwable cause) {
    super(context, cause, HttpStatusCodes.CONFLICT);
  }

  public ODataConflictException(final MessageReference context, final String errorCode) {
    super(context, HttpStatusCodes.CONFLICT, errorCode);
  }

  public ODataConflictException(final MessageReference context, final Throwable cause, final String errorCode) {
    super(context, cause, HttpStatusCodes.CONFLICT, errorCode);
  }

}
