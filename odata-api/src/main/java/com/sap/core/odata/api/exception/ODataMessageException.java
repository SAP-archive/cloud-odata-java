package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * Base exception class for all exceptions in <code>OData</code> library which contains a message which will be displayed to 
 * a possible client and therefore needs support for internationalization.
 * For support of internationalization and translated message all {@link ODataMessageException} and sub classes contains 
 * an {@link MessageReference} object which can be mapped to a related key and message in the resource bundles.
 */
public abstract class ODataMessageException extends ODataException {

  /** Context of exception which is used for internationalization */
  protected final MessageReference context;

  /**   */
  private static final long serialVersionUID = 42L;

  public static final MessageReference COMMON = createContext(ODataMessageException.class, "COMMON");

  public ODataMessageException(MessageReference context) {
    this.context = context;
  }

  /**
   * Convenience method for creation of {@link MessageReference} objects.
   * 
   * @param clazz
   *        Exception class of context
   * @param contextId
   *        Unique (in exception class) key for context.
   * @return created context instance
   */
  public static final MessageReference createContext(Class<? extends ODataMessageException> clazz, String contextId) {
    return MessageReference.create(clazz, contextId);
  }
  
  /**
   * Convenience method for creation of {@link MessageReference} objects.
   * 
   * @param clazz
   *        Exception class of context
   * @param contextId
   *        Unique (in exception class) key for context.
   * @param status
   *        {@link HttpStatus} of this {@link MessageReference}
   * @return created context instance
   */
  public static final MessageReference createContext(Class<? extends ODataMessageException> clazz, String contextId, HttpStatus status) {
    return MessageReference.create(clazz, contextId, status);
  }

  /**
   * Get the related {@link MessageReference}
   * 
   * @return the context
   */
  public MessageReference getContext() {
    return context;
  }
}
