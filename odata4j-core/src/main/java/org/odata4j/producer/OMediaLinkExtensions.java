package org.odata4j.producer;

import org.odata4j.core.OExtension;

/**
 * A factory for creating OMediaLinkExtension implementations to handle
 * requests related to media link entries.
 */
public interface OMediaLinkExtensions extends OExtension<ODataProducer> {

  /**
   * Creates an OMediaLinkExtension object to handle the given media link entry
   * request.
   * 
   * @param context
   * @return The OMediaLinkExtension
   */
  OMediaLinkExtension create(ODataContext context);

}
