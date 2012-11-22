package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an association set in the EDM
 */
public class AssociationSet {

  private String name;
  private FullQualifiedName association;
  private AssociationSetEnd end1;
  private AssociationSetEnd end2;
  private Documentation documentation;
  private Annotations annotations;

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
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the name for this {@link AssociationSet}
   * @param name
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} association for this {@link AssociationSet}
   * @param association
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setAssociation(FullQualifiedName association) {
    this.association = association;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the first {@link AssociationSetEnd} for this {@link AssociationSet}
   * @param end1
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setEnd1(AssociationSetEnd end1) {
    this.end1 = end1;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the second {@link AssociationSetEnd} for this {@link AssociationSet}
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
   * Sets the {@link Annotations} for this {@link AssociationSet}
   * @param annotations
   * @return {@link AssociationSet} for method chaining
   */
  public AssociationSet setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}