package com.sap.core.odata.api.exception;

public class ODataNotFoundException extends ODataMessageException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final Context USER = createContext(ODataNotFoundException.class, "USER");

  public ODataNotFoundException(Context context) {
    super(context);
  }

}
