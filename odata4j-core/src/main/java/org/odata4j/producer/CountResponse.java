package org.odata4j.producer;

/**
 * An <code>CountResponse</code> is a response to a client request for the count of an entity-set.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>CountResponse</code> instances.</p>
 */
public interface CountResponse extends BaseResponse {

  /** Gets the number of entities in an entity-set. */
  long getCount();

}
