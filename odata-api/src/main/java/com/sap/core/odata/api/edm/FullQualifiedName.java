package com.sap.core.odata.api.edm;

public class FullQualifiedName {

  private String name;
  private String namespace;

  public FullQualifiedName(String name, String namespace) {
    this.name = name;
    this.namespace = namespace;
  }

  public String getName() {
    return name;
  }

  public String getNamespace() {
    return namespace;
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