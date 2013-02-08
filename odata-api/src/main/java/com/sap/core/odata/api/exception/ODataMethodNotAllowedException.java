package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataMethodNotAllowedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference DISPATCH = createMessageReference(ODataMethodNotAllowedException.class, "DISPATCH");
  public static final MessageReference TUNNELING = createMessageReference(ODataMethodNotAllowedException.class, "TUNNELING");

  public ODataMethodNotAllowedException(MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  public ODataMethodNotAllowedException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  public ODataMethodNotAllowedException(MessageReference messageReference, String errorCode) {
    super(messageReference, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

  public ODataMethodNotAllowedException(MessageReference messageReference, Throwable cause, String errorCode) {
    super(messageReference, cause, HttpStatusCodes.METHOD_NOT_ALLOWED, errorCode);
  }

}
