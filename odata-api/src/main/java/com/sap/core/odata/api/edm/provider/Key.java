/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent a key for an entity type
 * @author SAP AG
 */
public class Key {

  private List<PropertyRef> keys;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return List<{@link PropertyRef}> references to the key properties
   */
  public List<PropertyRef> getKeys() {
    return keys;
  }

  /**
   * @return List of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return List of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * Sets the {@link Property}s by their {@link PropertyRef} for this {@link Key}
   * @param keys
   * @return {@link Key} for method chaining
   */
  public Key setKeys(final List<PropertyRef> keys) {
    this.keys = keys;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link Key}
   * @param annotationAttributes
   * @return {@link Key} for method chaining
   */
  public Key setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link Key}
   * @param annotationElements
   * @return {@link Key} for method chaining
   */
  public Key setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}