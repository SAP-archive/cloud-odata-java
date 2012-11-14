package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

public class ODataBadRequestException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataBadRequestException.class, "COMMON");
  public static final MessageReference URLTOSHORT = createMessageReference(ODataBadRequestException.class, "URLTOSHORT");

  public ODataBadRequestException(MessageReference context) {
    super(context, HttpStatusCodes.BAD_REQUEST);
  }
}
