package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataForbiddenException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataForbiddenException.class, "COMMON");

  public ODataForbiddenException(MessageReference context) {
    super(context, HttpStatusCodes.FORBIDDEN);
  }
  
  public ODataForbiddenException(MessageReference context, Throwable cause) {
    super(context, cause, HttpStatusCodes.FORBIDDEN);
  }
  
  public ODataForbiddenException(MessageReference context, String errorCode) {
    super(context, HttpStatusCodes.FORBIDDEN, errorCode);
  }
  
  public ODataForbiddenException(MessageReference context, Throwable cause, String errorCode) {
    super(context, cause, HttpStatusCodes.FORBIDDEN, errorCode);
  }
}
