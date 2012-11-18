package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

public class Key {

  private Collection<PropertyRef> keys;
  private Annotations annotations;

  public Collection<PropertyRef> getKeys() {
    return keys;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public Key setKeys(Collection<PropertyRef> keys) {
    this.keys = keys;
    return this;
  }

  public Key setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}