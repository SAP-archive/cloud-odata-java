package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent an association set end
 * @author SAP AG
 */
public class AssociationSetEnd {

  private String role;
  private String entitySet;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> role
   */
  public String getRole() {
    return role;
  }

  /**
   * @return <b>String</b> name of the target entity set
   */
  public String getEntitySet() {
    return entitySet;
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
   * Sets the role of this {@link AssociationSetEnd}
   * @param role
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setRole(String role) {
    this.role = role;
    return this;
  }

  /**
   * Sets the target entity set of this {@link AssociationSetEnd}
   * @param entitySet
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link AssociationSetEnd}
   * @param documentation
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link AssociationSetEnd}
   * @param annotationAttributes
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link AssociationSetEnd}
   * @param annotationElements
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}