package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public class ODataNotFoundException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final Context ENTITY = createContext(ODataNotFoundException.class, "ENTITY", HttpStatus.NOT_FOUND);

  public ODataNotFoundException(Context context) {
    super(context);
  }
}
