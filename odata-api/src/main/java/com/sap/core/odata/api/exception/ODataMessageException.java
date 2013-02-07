package com.sap.core.odata.api.exception;

/**
 * Base exception class for all exceptions in the <code>OData</code> library
 * which contains a message which will be displayed to a possible client and therefore
 * needs support for internationalization.  To support internationalization and translation of messages,
 * {@link ODataHttpException} and its sub classes contain a {@link MessageReference} object
 * which can be mapped to a related key and message in the resource bundles.
 * @author SAP AG
 */
public abstract class ODataMessageException extends ODataException {

  /** Message reference for exception which is used for internationalization */
  protected final MessageReference messageReference;
  /** OData error code*/
  private String errorCode;


  /**   */
  private static final long serialVersionUID = 42L;

  /** Reference to common message for a {@link ODataHttpException} */
  public static final MessageReference COMMON = createMessageReference(ODataHttpException.class, "COMMON");

  /**
   * Create {@link ODataHttpException} with given {@link MessageReference}.
   * 
   * @param messageReference
   *        references the message text (and additional values) of this {@link ODataHttpException}
   */
  public ODataMessageException(MessageReference messageReference) {
    this.messageReference = messageReference;
  }

  /**
   * Create {@link ODataHttpException} with given {@link MessageReference} and cause {@link Throwable} which caused
   * this {@link ODataHttpException}.
   * 
   * @param messageReference
   *        references the message text (and additional values) of this {@link ODataHttpException}
   * @param cause
   *        exception which caused this {@link ODataMessageException}
   */
  public ODataMessageException(MessageReference messageReference, Throwable cause) {
    super(cause);
    this.messageReference = messageReference;
  }
  
  /**
   * Create {@link ODataHttpException} with given {@link MessageReference}, cause {@link Throwable} and error code
   * @param messageReference
   * @param cause
   * @param errorCode
   */
  public ODataMessageException(MessageReference messageReference, Throwable cause, String errorCode) {
    this(messageReference, cause);
    this.errorCode = errorCode;
  }
  
  /**
   * Create {@link ODataHttpException} with given {@link MessageReference} and error code
   * @param messageReference
   * @param errorCode
   */
  public ODataMessageException(MessageReference messageReference, String errorCode) {
    this(messageReference);
    this.errorCode = errorCode;
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
  protected static final MessageReference createMessageReference(Class<? extends ODataMessageException> clazz, String messageReferenceKey) {
    return MessageReference.create(clazz, messageReferenceKey);
  }

  /**
   * Get the related {@link MessageReference}
   * 
   * @return the context
   */
  public MessageReference getMessageReference() {
    return messageReference;
  }
  
  
  /**
   * Get the error code for this {@link ODataMessageException}. Default is null.
   * @return the error code
   */
  public String getErrorCode() {
    return errorCode;
  }
}
