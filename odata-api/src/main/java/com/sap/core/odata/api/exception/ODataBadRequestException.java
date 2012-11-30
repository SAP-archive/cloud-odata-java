package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataBadRequestException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataBadRequestException.class, "COMMON");
  public static final MessageReference URLTOSHORT = createMessageReference(ODataBadRequestException.class, "URLTOSHORT");

  public ODataBadRequestException(MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.BAD_REQUEST);
  }

  public ODataBadRequestException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.BAD_REQUEST);
  }
}
