package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * @author SAP AG
 *
 */
public class Key {

  private Collection<PropertyRef> keys;
  private Annotations annotations;

  /**
   * @return Collection<{@link PropertyRef}> references to the key properties
   */
  public Collection<PropertyRef> getKeys() {
    return keys;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link Property}s by their {@link PropertyRef} for this {@link Key}
   * @param keys
   * @return {@link Key} for method chaining
   */
  public Key setKeys(Collection<PropertyRef> keys) {
    this.keys = keys;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link Key}
   * @param annotations
   * @return {@link Key} for method chaining
   */
  public Key setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}