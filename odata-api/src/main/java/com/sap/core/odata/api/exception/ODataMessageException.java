package com.sap.core.odata.api.exception;

import com.sap.core.odata.api.enums.HttpStatus;

/**
 * Base exception class for all exceptions in <code>OData</code> library which contains a message which will be displayed to 
 * a possible client and therefore needs support for internationalization.
 * For support of internationalization and translated message all {@link ODataMessageException} and sub classes contains 
 * a {@link MessageReference} object which can be mapped to a related key and message in the resource bundles.
 */
public abstract class ODataMessageException extends ODataException {

  /** Message reference for exception which is used for internationalization */
  protected final MessageReference messageReference;

  /**   */
  private static final long serialVersionUID = 42L;

  /** Reference to common message for a {@link ODataMessageException} */
  public static final MessageReference COMMON = createMessageReference(ODataMessageException.class, "COMMON");

  /**
   * Create {@link ODataMessageException} with given {@link MessageReference}.
   * 
   * @param messageReference
   *        references the message text (and additional values) of this {@link ODataMessageException}
   */
  public ODataMessageException(MessageReference messageReference) {
    this(messageReference, null);
  }

  /**
   * Create {@link ODataMessageException} with given {@link MessageReference} and cause {@link Throwable} which caused
   * this {@link ODataMessageException}.
   * 
   * @param messageReference
   *        references the message text (and additional values) of this {@link ODataMessageException}
   * @param cause
   *        exception which caused this {@link ODataMessageException}
   */
  public ODataMessageException(MessageReference messageReference, Throwable cause) {
    super(cause);
    this.messageReference = messageReference;
  }

  /**
   * Convenience method for creation of {@link MessageReference} objects.
   * 
   * @param clazz
   *        Exception class for message reference
   * @param messageReferenceKey
   *        Unique (in exception class) key for message reference.
   * @return created context instance
   */
  public static final MessageReference createMessageReference(Class<? extends ODataMessageException> clazz, String messageReferenceKey) {
    return MessageReference.create(clazz, messageReferenceKey);
  }
  
  /**
   * Convenience method for creation of {@link MessageReference} objects.
   * 
   * @param clazz
   *        Exception class for message reference
   * @param messageReferenceKey
   *        Unique (in exception class) key for message reference.
   * @param status
   *        {@link HttpStatus} of this {@link MessageReference}
   * @return created context instance
   */
  public static final MessageReference createContext(Class<? extends ODataMessageException> clazz, String messageReferenceKey, HttpStatus status) {
    return MessageReference.create(clazz, messageReferenceKey, status);
  }

  /**
   * Get the related {@link MessageReference}
   * 
   * @return the context
   */
  public MessageReference getMessageReference() {
    return messageReference;
  }
}
