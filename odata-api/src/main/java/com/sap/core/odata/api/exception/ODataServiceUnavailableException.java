package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 503 service unavailable
 * @author SAP AG
 */
public class ODataServiceUnavailableException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataServiceUnavailableException.class, "COMMON");

  public ODataServiceUnavailableException(MessageReference context) {
    super(context, HttpStatusCodes.SERVICE_UNAVAILABLE);
  }

  public ODataServiceUnavailableException(MessageReference context, Throwable cause) {
    super(context, cause, HttpStatusCodes.SERVICE_UNAVAILABLE);
  }

  public ODataServiceUnavailableException(MessageReference context, String errorCode) {
    super(context, HttpStatusCodes.SERVICE_UNAVAILABLE, errorCode);
  }

  public ODataServiceUnavailableException(MessageReference context, Throwable cause, String errorCode) {
    super(context, cause, HttpStatusCodes.SERVICE_UNAVAILABLE, errorCode);
  }

}
