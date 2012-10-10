package org.odata4j.core;

/**
 * A consumer-side entity-request builder, used for DELETE operations of a single entity.  Call {@link #execute()} to issue the request.
 */
public interface OEntityDeleteRequest extends OEntityRequest<Void> {

  /**
   * Overrides the If-Match precondition.
   *
   * <p>The If-Match header will default to the entity-tag of the entity used to start the builder sequence.</p>
   *
   * @param precondition  <code>null</code>, an entity-tag, or <code>*</code>
   * @return the modification-request builder
   */
  OEntityDeleteRequest ifMatch(String precondition);

}
