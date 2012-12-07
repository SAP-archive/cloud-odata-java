package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * Objects of this class represent a reference to a property via its name
 * @author SAP AG
 *
 */
public class PropertyRef {

  private String name;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name of the {@link Property} this {@link PropertyRef} is referencing to
   */
  public String getName() {
    return name;
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
   * <p>Sets the name of the {@link Property} this {@link PropertyRef} is pointing to
   * @param name
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link PropertyRef}
   * @param annotationAttributes
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link PropertyRef}
   * @param annotationElements
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}