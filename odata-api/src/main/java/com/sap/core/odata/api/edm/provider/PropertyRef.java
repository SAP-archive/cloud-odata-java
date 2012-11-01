package com.sap.core.odata.api.edm.provider;

public class PropertyRef {

  private String name;
  private Annotations annotations;

  public PropertyRef(String name, Annotations annotations) {
    this.name = name;
    this.annotations = annotations;
  }

  public String getName() {
    return name;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}