package org.odata4j.core;

import org.odata4j.exceptions.ODataProducerException;

/**
 * A consumer-side modification-request builder, used for operations such as MERGE and UPDATE.  Call {@link #execute()} to issue the request.
 *
 * @param <T>  the entity representation as a java type
 */
public interface OModifyRequest<T> {

  /**
   * Sets properties on the new entity.
   *
   * @param props  the properties
   * @return the modification-request builder
   */
  OModifyRequest<T> properties(OProperty<?>... props);

  /**
   * Sets properties on the new entity.
   *
   * @param props  the properties
   * @return the modification-request builder
   */
  OModifyRequest<T> properties(Iterable<OProperty<?>> props);

  /**
   * Defines an explicit link to another related entity.
   *
   * @param navProperty  the entity's relationship navigation property
   * @param target  the link target entity
   * @return the modification-request builder
   */
  OModifyRequest<T> link(String navProperty, OEntity target);

  /**
   * Defines an explicit link to another related entity.
   *
   * @param navProperty  the entity's relationship navigation property
   * @param targetKey  the key of the link target entity
   * @return the modification-request builder
   */
  OModifyRequest<T> link(String navProperty, OEntityKey targetKey);

  /**
   * Sends the modification request to the OData service.
   *
   * @throws ODataProducerException  error from the producer
   */
  void execute() throws ODataProducerException;

  /**
   * Selects a new modification entity by navigating to a referenced entity in a child collection.
   *
   * @param navProperty  the child collection
   * @param key  the referenced entity's key
   * @return the modification-request builder
   */
  OModifyRequest<T> nav(String navProperty, OEntityKey key);

  /**
   * Overrides the If-Match precondition.
   *
   * <p>The If-Match header will default to the entity-tag of the entity used to start the builder sequence.</p>
   *
   * @param precondition  <code>null</code>, an entity-tag, or <code>*</code>
   * @return the modification-request builder
   */
  OModifyRequest<T> ifMatch(String precondition);

}
