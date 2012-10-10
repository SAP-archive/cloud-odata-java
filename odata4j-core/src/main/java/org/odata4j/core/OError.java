package org.odata4j.core;

/**
 * An OData error message, consisting of an error code, an error-message text,
 * and an optional inner error.
 */
public interface OError {

  /**
   * Gets the error code, a mandatory service-defined string.
   *
   * <p>This value may be used to provide a more specific substatus to the returned HTTP response code.
   *
   * @return the error code
   */
  String getCode();

  /**
   * Gets the error-message text, a human readable message describing the error.
   *
   * @return the error-message text
   */
  String getMessage();

  /**
   * Gets the inner error, service specific debugging information that might assist a service implementer in determining the cause of an error.
   *
   * <p>Should only be used in development environments in order to guard against potential security concerns around information disclosure.
   *
   * @return the inner error
   */
  String getInnerError();
}
