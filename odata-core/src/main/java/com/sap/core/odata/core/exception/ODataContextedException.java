package com.sap.core.odata.core.exception;

public abstract class ODataContextedException extends ODataException {

  protected final Context context;

  /**
   * 
   */
  private static final long serialVersionUID = 42L;

  public static final Context COMMON = createContext(ODataContextedException.class, "COMMON");

  public ODataContextedException(Context context) {
    this.context = context;
  }

  public static final Context createContext(Class<? extends ODataContextedException> clazz, String context) {
    return Context.create(clazz, context);
  }
}
