package org.odata4j.producer;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.odata4j.core.OExtension;
import org.odata4j.exceptions.ODataProducerException;

/**
 * An optional extension that a producer can expose to control error responses.
 *
 * <p>To expose this extension, the producer implementation has to return an instance of this
 * interface when method {@code findExtension} is called and the first parameter is equal to
 * {@code ErrorResponseExtension.class}.</p>
 *
 * @see ErrorResponseExtensions
 */
public interface ErrorResponseExtension extends OExtension<ODataProducer> {

  /**
   * This method is called before an error response is created and sent to the client. Thus
   * producers can decide if an inner error should be returned or not. This decision can be based
   * on a system property, a query parameter, or any other data source.
   *
   * @param httpHeaders  the HTTP headers of the request that led to an error
   * @param uriInfo  the URI info of the request that led to an error
   * @param exception  the exception about to be sent to the client
   * @return flag indicating whether to return an inner error as part of the error response to the
   *         client or not
   */
  boolean returnInnerError(HttpHeaders httpHeaders, UriInfo uriInfo, ODataProducerException exception);
}
