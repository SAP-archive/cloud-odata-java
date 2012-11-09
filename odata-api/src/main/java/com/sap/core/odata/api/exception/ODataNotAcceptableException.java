package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataNotAcceptableException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataNotAcceptableException.class, "COMMON", HttpStatus.NOT_ACCEPTABLE);

  public ODataNotAcceptableException(MessageReference context) {
    super(context);
  }
}
