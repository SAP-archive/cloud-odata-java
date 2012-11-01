package com.sap.core.odata.api.edm.provider;

import java.util.List;

public class Key {

  private List<PropertyRef> keys;
  private Annotations annotations;
  
  public Key(List<PropertyRef> keys, Annotations annotations) {
    this.keys = keys;
    this.annotations = annotations;
  }

  public List<PropertyRef> getKeys() {
    return keys;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}