package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataPreconditionRequiredException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataPreconditionRequiredException.class, "COMMON");

  public ODataPreconditionRequiredException(MessageReference context) {
    super(context, HttpStatus.PRECONDITION_REQUIRED);
  }
}
