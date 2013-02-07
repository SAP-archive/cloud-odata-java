package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataMethodNotAllowedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference DISPATCH = createMessageReference(ODataMethodNotAllowedException.class, "DISPATCH");
  public static final MessageReference TUNNELING = createMessageReference(ODataMethodNotAllowedException.class, "TUNNELING");

  public ODataMethodNotAllowedException(MessageReference context) {
    super(context, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }
  
  public ODataMethodNotAllowedException(MessageReference context, Throwable cause) {
    super(context, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }
  
  public ODataMethodNotAllowedException(MessageReference context, String errorCode) {
    super(context, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }
  
  public ODataMethodNotAllowedException(MessageReference context, Throwable cause, String errorCode) {
    super(context, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

}
