package com.sap.core.odata.api.edm;

public class FullQualifiedName {

  private String namespace;
  private String name;

  public FullQualifiedName(String namespace, String name) {
    this.namespace = namespace;
    this.name = name;
  }

  public String getNamespace() {
    return namespace;
  }

  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || !(obj instanceof FullQualifiedName))
      return false;
    final FullQualifiedName other = (FullQualifiedName) obj;
    return namespace.equals(other.getNamespace()) && name.equals(other.getName());
  }

  @Override
  public String toString() {
    return namespace + "." + name;
  }
}