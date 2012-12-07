package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * Objects of this class represent a Key for an entity type
 * @author SAP AG
 */
public class Key {

  private Collection<PropertyRef> keys;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * @return Collection<{@link PropertyRef}> references to the key properties
   */
  public Collection<PropertyRef> getKeys() {
    return keys;
  }

  /**
   * @return collection of {@link AnnotationAttribute} annotation attributes
   */
  public Collection<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public Collection<AnnotationElement> getAnnotationElements() {
    return annotationElements;
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
   * Sets the collection of {@link AnnotationAttribute} for this {@link Key}
   * @param annotationAttributes
   * @return {@link Key} for method chaining
   */
  public Key setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Key}
   * @param annotationElements
   * @return {@link Key} for method chaining
   */
  public Key setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}