package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent documentation
 * @author SAP AG
 */
public class Documentation {

  private String summary;
  private String longDescription;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> summary
   */
  public String getSummary() {
    return summary;
  }

  /**
   * @return <b>String</b> the long description
   */
  public String getLongDescription() {
    return longDescription;
  }

  /**
   * @return collection of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * Sets the summary for this {@link Documentation}
   * @param summary
   * @return {@link Documentation} for method chaining
   */
  public Documentation setSummary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Sets the long description for this {@link Documentation}
   * @param longDescription
   * @return {@link Documentation} for method chaining
   */
  public Documentation setLongDescription(String longDescription) {
    this.longDescription = longDescription;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link Documentation}
   * @param annotationAttributes
   * @return {@link Documentation} for method chaining
   */
  public Documentation setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Documentation}
   * @param annotationElements
   * @return {@link Documentation} for method chaining
   */
  public Documentation setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}