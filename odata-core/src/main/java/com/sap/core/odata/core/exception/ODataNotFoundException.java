package com.sap.core.odata.core.exception;

public class ODataNotFoundException extends ODataContextedException {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  public static final Context USER = createContext(ODataNotFoundException.class, "USER");
  
  public ODataNotFoundException(Context context) {
    super(context);
  }

}
