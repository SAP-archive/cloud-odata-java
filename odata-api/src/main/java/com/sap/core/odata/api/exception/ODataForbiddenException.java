package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

public class ODataForbiddenException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataForbiddenException.class, "COMMON");

  public ODataForbiddenException(MessageReference context) {
    super(context, HttpStatusCodes.FORBIDDEN);
  }
}
