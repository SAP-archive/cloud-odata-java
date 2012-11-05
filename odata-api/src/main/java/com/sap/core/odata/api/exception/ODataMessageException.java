package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * Base exception class for all exceptions in <code>OData</code> library which contains a message which will be displayed to 
 * a possible client and therefore needs support for internationalization.
 * For support of internationalization and translated message all {@link ODataMessageException} and sub classes contains 
 * an {@link Context} object which can be mapped to a related key and message in the resource bundles.
 */
public abstract class ODataMessageException extends ODataException {

  /** Context of exception which is used for internationalization */
  protected final Context context;

  /**   */
  private static final long serialVersionUID = 42L;

  public static final Context COMMON = createContext(ODataMessageException.class, "COMMON");

  public ODataMessageException(Context context) {
    this.context = context;
  }

  /**
   * Convenience method for creation of {@link Context} objects.
   * 
   * @param clazz
   *        Exception class of context
   * @param contextId
   *        Unique (in exception class) key for context.
   * @return created context instance
   */
  public static final Context createContext(Class<? extends ODataMessageException> clazz, String contextId) {
    return Context.create(clazz, contextId);
  }
  
  /**
   * Convenience method for creation of {@link Context} objects.
   * 
   * @param clazz
   *        Exception class of context
   * @param contextId
   *        Unique (in exception class) key for context.
   * @param status
   *        {@link HttpStatus} of this {@link Context}
   * @return created context instance
   */
  public static final Context createContext(Class<? extends ODataMessageException> clazz, String contextId, HttpStatus status) {
    return Context.create(clazz, contextId, status);
  }

  /**
   * Get the related {@link Context}
   * 
   * @return the context
   */
  public Context getContext() {
    return context;
  }
}
