package org.odata4j.core;

import org.odata4j.exceptions.ODataProducerException;

/**
 * A consumer-side count-request builder. Call {@link #execute()} to issue the request.
 */
public interface OCountRequest {

  /**
   * Sets the name of the entity-set to count.
   *
   * @param entitySetName  the name of the entity collection
   * @return the count-request builder
   */
  OCountRequest entitySetName(String entitySetName);

  /**
   * Sets a maximum limit on the result returned.
   *
   * @param top  the maximum limit
   * @return the count-request builder
   */
  OCountRequest top(int top);

  /**
   * Sends the count-request to the OData service and returns the result.
   *
   * @return the count
   * @throws ODataProducerException  error from the producer
   */
  int execute() throws ODataProducerException;

}
