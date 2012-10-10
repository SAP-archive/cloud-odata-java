package org.odata4j.core;

import org.odata4j.exceptions.ODataProducerException;

/**
 * A consumer-side entity-request builder, used for operations on a single entity such as DELETE.  Call {@link #execute()} to issue the request.
 *
 * @param <T>  the java-type of the operation response
 */
public interface OEntityRequest<T> {

  /**
   * Sends the entity-request to the OData service and returns the response.
   *
   * @return the operation response
   * @throws ODataProducerException  error from the producer
   */
  T execute() throws ODataProducerException;

  /**
   * Navigates to a related entity using a collection navigation property.
   *
   * @param navProperty  the collection navigation property
   * @param key  the entity-key within the collection
   * @return the entity-request builder
   */
  OEntityRequest<T> nav(String navProperty, OEntityKey key);

  /**
   * Navigates to a related entity using a single-valued navigation property.
   *
   * @param navProperty  the single-valued collection navigation property.
   * @return the entity-request builder
   */
  OEntityRequest<T> nav(String navProperty);

}
