package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataForbiddenException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataForbiddenException.class, "COMMON", HttpStatus.FORBIDDEN);

  public ODataForbiddenException(MessageReference context) {
    super(context);
  }
}
