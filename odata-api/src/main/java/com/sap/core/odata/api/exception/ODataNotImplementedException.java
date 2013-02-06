package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataNotImplementedException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference COMMON = createMessageReference(ODataNotImplementedException.class, "COMMON");

  public ODataNotImplementedException(MessageReference context) {
    super(context, HttpStatusCodes.NOT_IMPLEMENTED);
  }

  public ODataNotImplementedException() {
    this(COMMON);
  }
}
