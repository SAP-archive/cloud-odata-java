package com.sap.core.odata.api.edm.provider;

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
}