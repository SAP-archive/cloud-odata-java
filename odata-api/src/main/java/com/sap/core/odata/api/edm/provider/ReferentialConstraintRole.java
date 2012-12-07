package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this Class represent a referential constraint role
 * @author SAP AG
 */
public class ReferentialConstraintRole {

  private String role;
  private PropertyRef propertyRef;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> role of this {@link ReferentialConstraintRole}
   */
  public String getRole() {
    return role;
  }

  /**
   * @return {@link PropertyRef} which this {@link ReferentialConstraintRole} points to
   */
  public PropertyRef getPropertyRef() {
    return propertyRef;
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
   * MANDATORY
   * <p> Sets the role of this {@link ReferentialConstraintRole}
   * @param role
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setRole(String role) {
    this.role = role;
    return this;
  }

  /**
   * MANDATORY
   * <p> Sets the {@link Property} of this {@link ReferentialConstraintRole} through a {@link PropertyRef}
   * @param propertyRef
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setPropertyRef(PropertyRef propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link ReferentialConstraintRole}
   * @param annotationAttributes
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link ReferentialConstraintRole}
   * @param annotationElements
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}