package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this Class represent a referential constraint
 * @author SAP AG
 */
public class ReferentialConstraint {

  private ReferentialConstraintRole principal;
  private ReferentialConstraintRole dependent;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return {@link ReferentialConstraintRole} the principal of this {@link ReferentialConstraint}
   */
  public ReferentialConstraintRole getPrincipal() {
    return principal;
  }

  /**
   * @return {@link ReferentialConstraintRole} the dependent of this {@link ReferentialConstraint}
   */
  public ReferentialConstraintRole getDependent() {
    return dependent;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
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
   * Sets the principal {@link ReferentialConstraintRole} for this {@link ReferentialConstraint}
   * @param principal
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setPrincipal(final ReferentialConstraintRole principal) {
    this.principal = principal;
    return this;
  }

  /**
   * Sets the dependent {@link ReferentialConstraintRole} for this {@link ReferentialConstraint}
   * @param dependent
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setDependent(final ReferentialConstraintRole dependent) {
    this.dependent = dependent;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link ReferentialConstraint}
   * @param documentation
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link ReferentialConstraint}
   * @param annotationAttributes
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link ReferentialConstraint}
   * @param annotationElements
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}