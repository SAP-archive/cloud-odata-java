package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an association in the EDM
 */
public class Association {

  private String name;
  private AssociationEnd end1;
  private AssociationEnd end2;
  private ReferentialConstraint referentialConstraint;
  private Documentation documentation;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link AssociationEnd} end2
   */
  public AssociationEnd getEnd1() {
    return end1;
  }

  /**
   * @return {@link AssociationEnd} end2
   */
  public AssociationEnd getEnd2() {
    return end2;
  }

  /**
   * @return {@link ReferentialConstraint} referentialConstraint
   */
  public ReferentialConstraint getReferentialConstraint() {
    return referentialConstraint;
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
   * <p>Sets the name for this {@link Association}
   * @param name
   * @return {@link Association} for method chaining
   */
  public Association setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the first {@link AssociationEnd} for this {@link Association}
   * @param end1
   * @return {@link Association} for method chaining
   */
  public Association setEnd1(AssociationEnd end1) {
    this.end1 = end1;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the second {@link AssociationEnd} for this {@link Association}
   * @param end2
   * @return {@link Association} for method chaining
   */
  public Association setEnd2(AssociationEnd end2) {
    this.end2 = end2;
    return this;
  }

  /**
   * Sets the {@link ReferentialConstraint} for this {@link Association}
   * @param referentialConstraint
   * @return {@link Association} for method chaining
   */
  public Association setReferentialConstraint(ReferentialConstraint referentialConstraint) {
    this.referentialConstraint = referentialConstraint;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link Association}
   * @param documentation
   * @return {@link Association} for method chaining
   */
  public Association setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link Association}
   * @param annotationAttributes
   * @return {@link Association} for method chaining
   */
  public Association setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Association}
   * @param annotationElements
   * @return {@link Association} for method chaining
   */
  public Association setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}