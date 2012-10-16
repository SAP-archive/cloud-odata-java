package com.sap.core.odata.core.experimental.edm.adapter;

public class EdmFQName {

  private String name;
  private String namespace;

  public EdmFQName(String fullQualifiedName) {
    int pos = fullQualifiedName.lastIndexOf(".");
    if (pos > -1) {
      this.namespace = fullQualifiedName.substring(0, pos);
      this.name = fullQualifiedName.substring(pos + 1);
    } else {
      this.name = fullQualifiedName;
    }
  }

  public String getName() {
    return name;
  }

  public String getNamespace() {
    return namespace;
  }
}
