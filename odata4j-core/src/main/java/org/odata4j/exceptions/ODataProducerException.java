package org.odata4j.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.odata4j.core.OError;
import org.odata4j.core.OErrors;

/**
 * An OData producer exception with the information described in the OData documentation for
 * <a href="http://www.odata.org/documentation/operations#ErrorConditions">error conditions</a>.
 *
 * <p>OData producer exceptions can be either created by using one of its sub-classes or by the
 * static factory {@link ODataProducerExceptions}.</p>
 */
public abstract class ODataProducerException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final OError error;

  /**
   * Constructor used by sub-classes to instantiate an exception that is thrown by an OData provider at runtime.
   *
   * <p>Parameters are delegated to {@link RuntimeException#RuntimeException(String, Throwable)}.</p>
   */
  protected ODataProducerException(String message, Throwable cause) {
    super(message, cause);
    error = OErrors.error(code(), message(), innerError());
  }

  /**
   * Constructor used by sub-classes to instantiate an exception based on the given OError
   * that has been received and parsed by an OData consumer.
   */
  protected ODataProducerException(OError error) {
    super(error.getMessage());
    this.error = error;
  }

  /**
   * Returns the code that is put into the OError object created during construction of this exception.
   *
   * <p>The default implementation returns the simple name of the underlying class. Sub-classes can override
   * this method and specify a different code.</p>
   *
   * @return the code
   * @see OError#getCode()
   */
  protected String code() {
    return getClass().getSimpleName();
  }

  /**
   * Returns the message that is put into the OError object created during construction of this exception.
   *
   * <p>The default implementation returns the exception's message ({@link RuntimeException#getMessage()}) if set.
   * Otherwise the reason phrase of the mapped HTTP status is returned ({@link StatusType#getReasonPhrase()}).
   * Sub-classes can override this method and specify a different message.</p>
   *
   * @return the message
   * @see OError#getMessage()
   */
  protected String message() {
    if (getMessage() != null)
      return getMessage();
    if (getHttpStatus() != null)
      return getHttpStatus().getReasonPhrase();
    return null;
  }

  /**
   * Returns the inner error that is put into the OError object created during construction of this exception.
   *
   * <p>The default implementation returns the exception's stack trace ({@link RuntimeException#printStackTrace(PrintWriter)}).
   * Sub-classes can override this method and specify a different inner error.</p>
   *
   * @return the inner error
   * @see OError#getInnerError()
   */
  protected String innerError() {
    StringWriter sw = new StringWriter();
    printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  /**
   * Gets the HTTP status.
   *
   * @return the HTTP status
   * @see Status
   */
  public abstract StatusType getHttpStatus();

  /**
   * Gets the OData error message.
   *
   * @return the OData error message
   */
  public OError getOError() {
    return error;
  }
}
