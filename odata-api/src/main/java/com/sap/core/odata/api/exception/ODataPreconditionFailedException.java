package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataPreconditionFailedException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataPreconditionFailedException.class, "COMMON", HttpStatus.PRECONDITION_FAILED);

  public ODataPreconditionFailedException(MessageReference context) {
    super(context);
  }
}
