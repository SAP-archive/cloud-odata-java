package com.sap.core.odata.api.exception;

/**
 * <p>DO NOT EXTEND THIS EXCEPTION</p>
 * APPLICATION DEVELOPERS: please use {@link ODataApplicationException} o throw custom exceptions.
 * <p>Base exception class for all exceptions in the <code>OData</code> library.
 * This class extends {@link ODataException} with a message that will be displayed
 * to a possible client and therefore needs support for internationalization.
 * <br>To support internationalization and translation of messages, this class
 * and its sub classes contain a {@link MessageReference} object which can be
 * mapped to a related key and message text in the resource bundles.
 * @author SAP AG
 */
public abstract class ODataMessageException extends ODataException {

  private static final long serialVersionUID = 42L;

  /** Message reference for exception which is used for internationalization */
  protected final MessageReference messageReference;
  /** OData error code */
  protected final String errorCode;

  /** Reference to common message for a {@link ODataMessageException} */
  public static final MessageReference COMMON = createMessageReference(ODataMessageException.class, "COMMON");

  /**
   * Creates {@link ODataMessageException} with given {@link MessageReference}.
   * @param messageReference references the message text (and additional values)
   *                         of this {@link ODataMessageException}
   */
  public ODataMessageException(MessageReference messageReference) {
    this(messageReference, null, null);
  }

  /**
   * Creates {@link ODataMessageException} with given {@link MessageReference}
   * and cause {@link Throwable} which caused this exception.
   * @param messageReference references the message text (and additional values)
   *                         of this {@link ODataMessageException}
   * @param cause            exception which caused this exception
   */
  public ODataMessageException(MessageReference messageReference, Throwable cause) {
    this(messageReference, cause, null);
  }

  /**
   * Creates {@link ODataMessageException} with given {@link MessageReference},
   * cause {@link Throwable} and error code.
   * @param messageReference references the message text (and additional values)
   *                         of this {@link ODataMessageException}
   * @param cause            exception which caused this exception
   * @param errorCode        a String with a unique code identifying this exception
   */
  public ODataMessageException(MessageReference messageReference, Throwable cause, String errorCode) {
    super(cause);
    this.messageReference = messageReference;
    this.errorCode = errorCode;
  }

  /**
   * Creates {@link ODataMessageException} with given {@link MessageReference} and error code.
   * @param messageReference references the message text (and additional values)
   *                         of this {@link ODataMessageException}
   * @param errorCode        a String with a unique code identifying this exception
   */
  public ODataMessageException(MessageReference messageReference, String errorCode) {
    this(messageReference, null, errorCode);
  }

  /**
   * Creates {@link MessageReference} objects more conveniently.
   * @param clazz               exception class for message reference
   * @param messageReferenceKey unique (in exception class) key for message reference
   * @return created message-reference instance
   */
  protected static final MessageReference createMessageReference(Class<? extends ODataMessageException> clazz, String messageReferenceKey) {
    return MessageReference.create(clazz, messageReferenceKey);
  }

  /**
   * Gets the related {@link MessageReference}.
   * @return the message reference
   */
  public MessageReference getMessageReference() {
    return messageReference;
  }

  /**
   * Gets the error code for this {@link ODataMessageException}.
   * Default is <code>null</code>.
   * @return the error code
   */
  public String getErrorCode() {
    return errorCode;
  }
}
