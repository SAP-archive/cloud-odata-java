package org.odata4j.core;

/**
 * An annotation (typed name-value pair) that lives in a namespace.
 */
public interface NamespacedAnnotation<T> extends NamedValue<T> {

  /** Gets the namespace (and namespace prefix) for this annotation. */
  PrefixedNamespace getNamespace();

  /** Gets the java-type of the annotation value. */
  Class<T> getValueType();

}
