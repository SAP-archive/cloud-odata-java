package org.odata4j.producer;

import org.odata4j.core.OProperty;

/**
 * An <code>PropertyResponse</code> is a response to a client request expecting a single property value.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>PropertyResponse</code> instances.</p>
 */
public interface PropertyResponse extends BaseResponse {

  /**
   * Gets the property value.
   *
   * @return the property value
   */
  OProperty<?> getProperty();

}
