package com.sap.core.odata.api.edm.provider;

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
  private Annotations annotations;

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
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
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

  //TODO: shouldnt this be start and end?
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
   * Sets the {@link Annotations} for this {@link Association}
   * @param annotations
   * @return {@link Association} for method chaining
   */
  public Association setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}