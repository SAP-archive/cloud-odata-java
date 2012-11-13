package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataBadRequestException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataBadRequestException.class, "COMMON", HttpStatus.BAD_REQUEST);
  public static final MessageReference URLTOSHORT = createContext(ODataBadRequestException.class, "URLTOSHORT", HttpStatus.BAD_REQUEST);

  public ODataBadRequestException(MessageReference context) {
    super(context);
  }
}
