package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Exceptions of this class will result in a HTTP status 404 not found
 * @author SAP AG
 */
public class ODataNotFoundException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference ENTITY = createMessageReference(ODataNotFoundException.class, "ENTITY");
  public static final MessageReference MATRIX = createMessageReference(ODataNotFoundException.class, "MATRIX");

  public ODataNotFoundException(MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.NOT_FOUND);
  }
  
  public ODataNotFoundException(MessageReference messageReference, String errorCode) {
    super(messageReference, HttpStatusCodes.NOT_FOUND, errorCode);
  }

  public ODataNotFoundException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.NOT_FOUND);
  }
  
  public ODataNotFoundException(MessageReference messageReference, Throwable cause, String errorCode) {
    super(messageReference, cause, HttpStatusCodes.NOT_FOUND, errorCode);
  }
}
