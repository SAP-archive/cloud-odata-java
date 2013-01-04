package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

public class ODataServiceUnavailableException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataServiceUnavailableException.class, "COMMON");

  public ODataServiceUnavailableException(MessageReference context) {
    super(context, HttpStatusCodes.SERVICE_UNAVAILABLE);
  }
}
