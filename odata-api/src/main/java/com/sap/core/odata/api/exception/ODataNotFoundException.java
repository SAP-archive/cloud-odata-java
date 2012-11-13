package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * @author SAP AG
 */
public class ODataNotFoundException extends ODataHttpException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference ENTITY = createMessageReference(ODataNotFoundException.class, "ENTITY");

  public ODataNotFoundException(MessageReference context) {
    super(context, HttpStatus.NOT_FOUND);
  }
}
