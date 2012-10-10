package org.odata4j.producer;

import org.odata4j.core.OEntity;

/**
 * An <code>EntityResponse</code> is a response to a client request expecting a single OData entity.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>EntityResponse</code> instances.</p>
 */
public interface EntityResponse extends BaseResponse {

  /**
   * Gets the OData entity.
   *
   * @return the entity
   */
  OEntity getEntity();
}
