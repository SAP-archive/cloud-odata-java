package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataServiceUnavailableException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataServiceUnavailableException.class, "COMMON");

  public ODataServiceUnavailableException(MessageReference context) {
    super(context, HttpStatus.SERVICE_UNAVAILABLE);
  }
}
