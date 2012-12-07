package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this Class represent a navigation property
 * @author SAP AG
 */
public class NavigationProperty {

  private String name;
  private FullQualifiedName relationship;
  private String fromRole;
  private String toRole;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name of this navigation property
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} of the relationship
   */
  public FullQualifiedName getRelationship() {
    return relationship;
  }

  /**
   * @return <b>String</b> name of the role this navigation is comming from
   */
  public String getFromRole() {
    return fromRole;
  }

  /**
   * @return <b>String</b> name of the role this navigation is going to
   */
  public String getToRole() {
    return toRole;
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
   * MANDATORY
   * <p>Sets the name of this {@link NavigationProperty}
   * @param name
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} for the relationship of this {@link NavigationProperty}
   * @param relationship
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setRelationship(FullQualifiedName relationship) {
    this.relationship = relationship;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the role this {@link NavigationProperty} is comming from
   * @param fromRole
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setFromRole(String fromRole) {
    this.fromRole = fromRole;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the role this {@link NavigationProperty} is going to
   * @param toRole
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setToRole(String toRole) {
    this.toRole = toRole;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link NavigationProperty}
   * @param documentation
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link NavigationProperty}
   * @param annotationAttributes
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link NavigationProperty}
   * @param annotationElements
   * @return {@link NavigationProperty} for method chaining
   */
  public NavigationProperty setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}