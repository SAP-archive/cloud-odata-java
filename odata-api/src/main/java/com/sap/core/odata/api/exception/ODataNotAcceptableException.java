package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 406 not acceptable
 * @author SAP AG
 */
public class ODataNotAcceptableException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataNotAcceptableException.class, "COMMON");
  public static final MessageReference NOT_SUPPORTED_CONTENT_TYPE = createMessageReference(ODataNotAcceptableException.class, "NOT_SUPPORTED_CONTENT_TYPE");

  public ODataNotAcceptableException(MessageReference context) {
    super(context, HttpStatusCodes.NOT_ACCEPTABLE);
  }

  public ODataNotAcceptableException(MessageReference context, Throwable cause) {
    super(context, cause, HttpStatusCodes.NOT_ACCEPTABLE);
  }

  public ODataNotAcceptableException(MessageReference context, String errorCode) {
    super(context, HttpStatusCodes.NOT_ACCEPTABLE, errorCode);
  }

  public ODataNotAcceptableException(MessageReference context, Throwable cause, String errorCode) {
    super(context, cause, HttpStatusCodes.NOT_ACCEPTABLE, errorCode);
  }
}
