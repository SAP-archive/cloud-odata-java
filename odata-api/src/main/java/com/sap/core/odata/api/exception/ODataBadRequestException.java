package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataBadRequestException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataBadRequestException.class, "COMMON");
  public static final MessageReference URLTOSHORT = createMessageReference(ODataBadRequestException.class, "URLTOSHORT");
  public static final MessageReference VERSIONERROR = createMessageReference(ODataBadRequestException.class, "VERSIONERROR");
  public static final MessageReference PARSEVERSIONERROR = createMessageReference(ODataBadRequestException.class, "PARSEVERSIONERROR");
  
  public ODataBadRequestException(MessageReference messageReference) {
    super(messageReference, HttpStatusCodes.BAD_REQUEST);
  }

  public ODataBadRequestException(MessageReference messageReference, Throwable cause) {
    super(messageReference, cause, HttpStatusCodes.BAD_REQUEST);
  }
}
