package com.sap.core.odata.api.edm;

/**
 * @com.sap.core.odata.DoNotImplement
 * A full qualified name of any element in the EDM consists of a name and a namespace.
 * @author SAP AG
 */
public class FullQualifiedName {

  private final String namespace;
  private final String name;

  /**
   * @param namespace
   * @param name
   */
  public FullQualifiedName(String namespace, String name) {
    this.namespace = namespace;
    this.name = name;
  }

  /**
   * @return namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || !(obj instanceof FullQualifiedName)) {
      return false;
    }
    final FullQualifiedName other = (FullQualifiedName) obj;
    return namespace.equals(other.getNamespace()) && name.equals(other.getName());
  }

  @Override
  public String toString() {
    return namespace + Edm.DELIMITER + name;
  }
}