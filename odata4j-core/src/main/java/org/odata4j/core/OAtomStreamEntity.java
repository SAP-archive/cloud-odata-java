package org.odata4j.core;

/**
 *  OEntity extension to support streaming entities.
 */
public interface OAtomStreamEntity extends OExtension<OEntity> {

  /**
   * Gets the stream content-type.
   */
  String getAtomEntityType();

  /**
   * Gets the stream src uri.
   */
  String getAtomEntitySource();

}