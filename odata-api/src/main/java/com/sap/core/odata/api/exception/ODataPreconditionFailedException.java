package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

public class ODataPreconditionFailedException extends ODataHttpException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataPreconditionFailedException.class, "COMMON");

  public ODataPreconditionFailedException(MessageReference context) {
    super(context, HttpStatusCodes.PRECONDITION_FAILED);
  }
}
