package com.sap.core.odata.core.exception;

public class ODataCustomerException extends Exception implements ODataException {

  /**
   * 
   */
  private static final long serialVersionUID = 42L;
  
  public static final Context COMMON = createContext(ODataCustomerException.class, "COMMON");
  
  public ODataCustomerException(Context context) {
  }
  
  public static final Context createContext(Class<? extends ODataCustomerException> clazz, String context) {
    return Context.create(clazz, context);
  }
}
