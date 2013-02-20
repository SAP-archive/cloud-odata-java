package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 409 Conflict
 * @author SAP AG
 */
public class ODataConflictException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataConflictException.class, "COMMON");

  public ODataConflictException(MessageReference context) {
    super(context, HttpStatusCodes.CONFLICT);
  }

  public ODataConflictException(MessageReference context, Throwable cause) {
    super(context, cause, HttpStatusCodes.CONFLICT);
  }

  public ODataConflictException(MessageReference context, String errorCode) {
    super(context, HttpStatusCodes.CONFLICT, errorCode);
  }

  public ODataConflictException(MessageReference context, Throwable cause, String errorCode) {
    super(context, cause, HttpStatusCodes.CONFLICT, errorCode);
  }

}
