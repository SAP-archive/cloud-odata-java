package org.odata4j.producer;

import org.odata4j.edm.EdmSimpleType;

/**
 * An <code>SimpleResponse</code> is a response to a client request expecting a single EdmSimpleType value.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>SimpleResponse</code> instances.</p>
 */
public interface SimpleResponse extends BaseResponse {

  /**
   * Gets the type of the value
   * @return the type of the value
   */
  EdmSimpleType getType();

  /**
   * Gets the value.
   *
   * @return the property value
   */
  Object getValue();

  /**
   * Gets the (optional) name of the value
   *  
   * @return the property name if available or null
   */
  String getName();

}
