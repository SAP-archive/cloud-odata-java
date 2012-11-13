package com.sap.core.odata.api.edm.provider;

import java.util.Map;

public class Key {

  private Map<String, PropertyRef> keys;
  private Annotations annotations;

  public Map<String, PropertyRef> getKeys() {
    return keys;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public Key setKeys(Map<String, PropertyRef> keys) {
    this.keys = keys;
    return this;
  }

  public Key setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
  
  
}