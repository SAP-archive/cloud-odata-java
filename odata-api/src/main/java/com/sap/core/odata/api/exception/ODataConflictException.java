package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataConflictException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataConflictException.class, "COMMON");

  public ODataConflictException(MessageReference context) {
    super(context, HttpStatus.CONFLICT);
  }
}
