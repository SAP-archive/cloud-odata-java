package com.sap.core.odata.api.edm.provider;

public class PropertyRef {

  private String name;
  private Annotations annotations;

  public String getName() {
    return name;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public PropertyRef setName(String name) {
    this.name = name;
    return this;
  }

  public PropertyRef setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}