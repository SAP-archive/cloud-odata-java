package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent a reference to a property via its name
 * @author SAP AG
 *
 */
public class PropertyRef {

  private String name;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name of the {@link Property} this {@link PropertyRef} is referencing to
   */
  public String getName() {
    return name;
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
   * Sets the name of the {@link Property} this {@link PropertyRef} is pointing to
   * @param name
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setName(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link PropertyRef}
   * @param annotationAttributes
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link PropertyRef}
   * @param annotationElements
   * @return {@link PropertyRef} for method chaining
   */
  public PropertyRef setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}