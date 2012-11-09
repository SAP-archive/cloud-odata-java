package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataConflictException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataConflictException.class, "COMMON", HttpStatus.CONFLICT);

  public ODataConflictException(MessageReference context) {
    super(context);
  }
}
