package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataPreconditionFailedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataPreconditionFailedException.class, "COMMON");

  public ODataPreconditionFailedException(MessageReference context) {
    super(context, HttpStatusCodes.PRECONDITION_FAILED);
  }
  
  public ODataPreconditionFailedException(MessageReference context, Throwable cause) {
    super(context, cause, HttpStatusCodes.PRECONDITION_FAILED);
  }
  
  public ODataPreconditionFailedException(MessageReference context, String errorCode) {
    super(context, HttpStatusCodes.PRECONDITION_FAILED, errorCode);
  }
  
  public ODataPreconditionFailedException(MessageReference context, Throwable cause, String errorCode) {
    super(context, cause, HttpStatusCodes.PRECONDITION_FAILED, errorCode);
  }
}
