package org.odata4j.core;

/**
 * Generic implementation of {@link NamespacedAnnotation}.
 */
public class GenericAnnotation<T> implements NamespacedAnnotation<T> {

  private final PrefixedNamespace namespace;
  private final String localName;
  private final Class<T> valueType;
  private final T value;

  public GenericAnnotation(String namespaceUri, String namespacePrefix, String localName, Class<T> valueType, T value) {
    this.namespace = new PrefixedNamespace(namespaceUri, namespacePrefix);
    this.localName = localName;
    this.valueType = valueType;
    this.value = value;
  }

  @Override
  public PrefixedNamespace getNamespace() {
    return namespace;
  }

  @Override
  public String getName() {
    return localName;
  }

  public String getFullyQualifiedName() {
    return namespace.getPrefix() + ":" + localName;
  }

  @Override
  public Class<T> getValueType() {
    return valueType;
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "Annotation: " + namespace + ":" + localName + " = " + value;
  }
}
