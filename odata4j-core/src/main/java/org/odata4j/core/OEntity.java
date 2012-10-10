package org.odata4j.core;

import java.util.List;

import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;

/**
 * An immutable OData entity instance, consisting of an identity (an entity-set and a unique entity-key within that set),
 * properties (typed, named values), and links (references to other entities).
 *
 * <p>The {@link OEntities} static factory class can be used to create <code>OEntity</code> instances.</p>
 *
 * @see OEntities
 */
public interface OEntity extends OEntityId, OStructuralObject, OExtensible<OEntity> {

  /**
   * Gets the entity-set of this instance.
   *
   * @return the entity-set
   */
  EdmEntitySet getEntitySet();

  /**
   * Gets the entity type of this instance.  This will either be the declared
   * entity type of the entity set associated with this instance *or* a sub-type
   * of that entity type.
   *
   * @return the entity-type
   */
  EdmEntityType getEntityType();

  /**
   * An optional opaque token used for concurrency control.
   *
   * @return the entity-tag
   */
  String getEntityTag();

  /**
   * Gets all links of this instance.
   *
   * @return the links
   */
  List<OLink> getLinks();

  /**
   * Gets a link with a given name and link-type.
   *
   * @param <T>  the link-type as a java-type
   * @param title  the link title
   * @param linkClass  the link-type as a java-type
   * @return the link strongly-typed as the java-type
   */
  <T extends OLink> T getLink(String title, Class<T> linkClass);

}
