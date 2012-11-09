package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataServiceUnavailableException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createContext(ODataServiceUnavailableException.class, "COMMON", HttpStatus.SERVICE_UNAVAILABLE);

  public ODataServiceUnavailableException(MessageReference context) {
    super(context);
  }
}
