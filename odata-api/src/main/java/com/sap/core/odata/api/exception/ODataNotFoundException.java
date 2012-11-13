package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * @author SAP AG
 */
public class ODataNotFoundException extends ODataMessageException {

  private static final long serialVersionUID = 1L;

  public static final MessageReference ENTITY = createContext(ODataNotFoundException.class, "ENTITY", HttpStatus.NOT_FOUND);

  public ODataNotFoundException(MessageReference context) {
    super(context);
  }
}
