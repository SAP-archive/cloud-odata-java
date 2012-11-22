package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class ODataNotFoundException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference ENTITY = createMessageReference(ODataNotFoundException.class, "ENTITY");

  public static final MessageReference MATRIX = createMessageReference(ODataNotFoundException.class, "MATRIX");

  public ODataNotFoundException(MessageReference context) {
    super(context, HttpStatusCodes.NOT_FOUND);
  }
}
