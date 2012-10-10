package org.odata4j.core;

import java.util.List;

/**
 * An <code>ORelatedEntitiesLinkInline</code> is an {@link ORelatedEntitiesLink} that also includes the referents inline.
 *
 * <p>The {@link OLinks} static factory class can be used to create <code>ORelatedEntitiesLink</code> instances.</p>
 *
 * @see OLinks
 */
public interface ORelatedEntitiesLinkInline extends ORelatedEntitiesLink {

  /**
   * Gets the inlined referents.
   */
  List<OEntity> getRelatedEntities();

}
