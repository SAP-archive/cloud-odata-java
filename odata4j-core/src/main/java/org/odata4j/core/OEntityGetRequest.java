package org.odata4j.core;

/**
 * A consumer-side entity-request builder, used for GET operations of a single entity.  Call {@link #execute()} to issue the request.
 *
 * @param <T>  the java-type of the operation response
 */
public interface OEntityGetRequest<T> extends OEntityRequest<T> {

  /**
   * Specifies a subset of properties to return.
   *
   * @param select  a comma-separated list of selection clauses
   * @return the entity-request builder
   */
  OEntityGetRequest<T> select(String select);

  /**
   * Specifies related entities to expand inline as part of the response.
   *
   * @param expand  a comma-separated list of navigation properties
   * @return the entity-request builder
   */
  OEntityGetRequest<T> expand(String expand);

}
