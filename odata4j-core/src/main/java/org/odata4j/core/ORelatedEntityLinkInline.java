package org.odata4j.core;

/**
 * An <code>ORelatedEntityLinkInline</code> is an {@link ORelatedEntityLink} that also includes the referent inlined.
 *
 * <p>The {@link OLinks} static factory class can be used to create <code>ORelatedEntityLinkInline</code> instances.</p>
 *
 * @see OLinks
 */
public interface ORelatedEntityLinkInline extends ORelatedEntityLink {

  /**
   * Gets the inlined referent.
   */
  OEntity getRelatedEntity();
}
