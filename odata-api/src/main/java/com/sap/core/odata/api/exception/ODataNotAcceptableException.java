package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

public class ODataNotAcceptableException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataNotAcceptableException.class, "COMMON");
  public static final MessageReference NOT_SUPPORTED_CONTENT_TYPE = createMessageReference(ODataNotAcceptableException.class, "NOT_SUPPORTED_CONTENT_TYPE");

  public ODataNotAcceptableException(MessageReference context) {
    super(context, HttpStatusCodes.NOT_ACCEPTABLE);
  }
}
