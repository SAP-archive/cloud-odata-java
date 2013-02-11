package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent an association set
 * @author SAP AG
 */
public class AssociationSet {

  private String name;
  private FullQualifiedName association;
  private AssociationSetEnd end1;
  private AssociationSetEnd end2;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} Association of this {@link AssociationSet} (namespace and name)
   */
  public FullQualifiedName getAssociation() {
    return association;
  }

  /**
   * @return {@link AssociationEnd} end1
   */
  public AssociationSetEnd getEnd1() {
    return end1;
  }

  /**
   * @return {@link AssociationEnd} end2
   */
  public AssociationSetEnd getEnd2() {
    return end2;
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
   * Sets the name for this {@link AssociationSet}
   * @param name
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the {@link FullQualifiedName} association for this {@link AssociationSet}
   * @param association
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setAssociation(FullQualifiedName association) {
    this.association = association;
    return this;
  }

  /**
   * Sets the first {@link AssociationSetEnd} for this {@link AssociationSet}
   * @param end1
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setEnd1(AssociationSetEnd end1) {
    this.end1 = end1;
    return this;
  }

  /**
   * Sets the second {@link AssociationSetEnd} for this {@link AssociationSet}
   * @param end2
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setEnd2(AssociationSetEnd end2) {
    this.end2 = end2;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link AssociationSet}
   * @param documentation
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link AssociationSet}
   * @param annotationAttributes
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setAnnotationAttributes(List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link AssociationSet}
   * @param annotationElements
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setAnnotationElements(List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}