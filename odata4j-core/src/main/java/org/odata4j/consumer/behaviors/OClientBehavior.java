package org.odata4j.consumer.behaviors;

import org.odata4j.consumer.ODataClientRequest;

/**
 * Extension-point for modifying client http requests.
 *
 * <p>The {@link OClientBehaviors} static factory class can be used to create built-in <code>OClientBehavior</code> instances.</p>
 */
public interface OClientBehavior {

  /**
   * Transforms the current http request.
   *
   * @param request  the current http request
   * @return the modified http request
   */
  ODataClientRequest transform(ODataClientRequest request);

}
