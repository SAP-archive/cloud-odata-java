package com.sap.core.odata.api.edm.provider;

import java.util.Map;

public class Key {

  private Map<String, PropertyRef> keys;
  private Annotations annotations;
  
  public Key(Map<String, PropertyRef> keys, Annotations annotations) {
    this.keys = keys;
    this.annotations = annotations;
  }

  public Map<String, PropertyRef> getKeys() {
    return keys;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}