package org.odata4j.exceptions;

import org.odata4j.core.OError;

/**
 * A factory for instances of a specific sub-class of {@link ODataProducerException}.
 *
 * <p>Implementations of this interface are registered to and used by {@link ODataProducerExceptions}.
 * When an OData consumer receives an error message returned by an OData producer, the parsed
 * {@link OError} is wrapped into an exception and thrown via the consumer API.</p>
 *
 * @param <T>  the concrete sub-class of {@link ODataProducerException} created
 * @see ODataProducerExceptions#create(javax.ws.rs.core.Response.StatusType, OError)
 */
public interface ExceptionFactory<T extends ODataProducerException> {

  /**
   * Gets the status code this factory creates exception instances for.
   *
   * @return the status code
   */
  int getStatusCode();

  /**
   * Creates an instance of a specific sub-class of {@link ODataProducerException}.
   *
   * @param error  the OData error message to be wrapped into the returned exception
   * @return the exception instance
   */
  T createException(OError error);
}
