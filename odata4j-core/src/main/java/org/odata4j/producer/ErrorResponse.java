package org.odata4j.producer;

import org.odata4j.core.OError;

/**
 * An <code>ErrorResponse</code> is the response in case an error occurred.
 */
public interface ErrorResponse extends BaseResponse {

  /**
   * Get the error.
   *
   * @return the error
   */
  OError getError();

}
