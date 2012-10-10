package org.odata4j.edm;

import org.odata4j.core.GenericAnnotation;

/**
 * Base annotation in the edm type system.
 *
 * <p>Either an {@link EdmAnnotationAttribute} or an {@link EdmAnnotationElement}.
 */
public abstract class EdmAnnotation<T> extends GenericAnnotation<T> {

  protected EdmAnnotation(String namespaceUri, String namespacePrefix, String name, Class<T> valueType, T value) {
    super(namespaceUri, namespacePrefix, name, valueType, value);
  }

  /** Creates an element annotation */
  public static <T> EdmAnnotationElement<T> element(String namespaceUri, String namespacePrefix, String name, Class<T> valueType, T value) {
    return new EdmAnnotationElement<T>(namespaceUri, namespacePrefix, name, valueType, value);
  }

  /** Creates an attribute annotation */
  public static EdmAnnotationAttribute attribute(String namespaceUri, String namespacePrefix, String name, String value) {
    return new EdmAnnotationAttribute(namespaceUri, namespacePrefix, name, value);
  }

}
