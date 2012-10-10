package org.odata4j.core;

import java.util.List;

/**
 * An <code>OLink</code> represents a reference to one or more OData entities.
 *
 * <p>Relationships to a single entity are represented as {@link ORelatedEntityLink}.
 * Relationships to multiple entities are represented as {@link ORelatedEntitiesLink}.  </p>
 *
 * <p>The {@link OLinks} static factory class can be used to create <code>OLink</code> instances.</p>
 *
 * @see OLinks
 */
public interface OLink extends Titled {

  /**
   * Gets the link title.
   *
   * @return the link title
   */
  String getTitle();

  /**
   * Gets the link relation.
   *
   * @return the link relation
   */
  String getRelation();

  /**
   * Gets the link href.
   *
   * @return the link href
   */
  String getHref();

  /**
   * Returns true if this OLink represents inlined content, false otherwise.
   */
  boolean isInline();

  /**
   * Returns true if this OLink represents a collection of related entities,
   * false if it is a single entity link.
   */
  boolean isCollection();

  /**
   * Gets the inlined referent.
   */
  OEntity getRelatedEntity();

  /**
   * Gets the inlined referents.
   */
  List<OEntity> getRelatedEntities();

}
