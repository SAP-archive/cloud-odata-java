package org.odata4j.core;

/**
 * Basic extension mechanism.
 *
 * @param <T>  the type being extended
 *
 * @see OExtension
 */
public interface OExtensible<T> {

  /**
   * Finds an extension instance given an interface, if one exists.
   *
   * @param clazz  the extension interface
   * @param <TExtension>  type of extension
   * @return the extension instance, or null if no extension exists for this type
   */
  <TExtension extends OExtension<T>> TExtension findExtension(Class<TExtension> clazz);

}
