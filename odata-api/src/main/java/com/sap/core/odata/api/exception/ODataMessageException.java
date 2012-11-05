package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

public abstract class ODataMessageException extends ODataException {

  protected final Context context;

  /**
   * 
   */
  private static final long serialVersionUID = 42L;

  public static final Context COMMON = createContext(ODataMessageException.class, "COMMON");

  public ODataMessageException(Context context) {
    this.context = context;
  }

  public static final Context createContext(Class<? extends ODataMessageException> clazz, String contextId) {
    return Context.create(clazz, contextId);
  }
  
  public static final Context createContext(Class<? extends ODataMessageException> clazz, String contextId, HttpStatus status) {
    return Context.create(clazz, contextId, status);
  }

  public Context getContext() {
    return context;
  }
}
