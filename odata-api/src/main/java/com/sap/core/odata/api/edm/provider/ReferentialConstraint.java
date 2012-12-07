package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * Objects of this Class represent a referential constraint
 * @author SAP AG
 */
public class ReferentialConstraint {

  private ReferentialConstraintRole principal;
  private ReferentialConstraintRole dependent;
  private Documentation documentation;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

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
   * <p>Sets the principal {@link ReferentialConstraintRole} for this {@link ReferentialConstraint}
   * @param principal
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setPrincipal(ReferentialConstraintRole principal) {
    this.principal = principal;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the dependent {@link ReferentialConstraintRole} for this {@link ReferentialConstraint}
   * @param dependent
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setDependent(ReferentialConstraintRole dependent) {
    this.dependent = dependent;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link ReferentialConstraint}
   * @param documentation
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link ReferentialConstraint}
   * @param annotationAttributes
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link ReferentialConstraint}
   * @param annotationElements
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}