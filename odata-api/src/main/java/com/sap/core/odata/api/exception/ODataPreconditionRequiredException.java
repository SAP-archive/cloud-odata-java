package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataPreconditionRequiredException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataPreconditionRequiredException.class, "COMMON", HttpStatus.PRECONDITION_REQUIRED);

  public ODataPreconditionRequiredException(MessageReference context) {
    super(context);
  }
}
