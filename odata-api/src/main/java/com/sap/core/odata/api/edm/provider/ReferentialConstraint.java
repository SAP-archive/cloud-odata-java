package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 * <p>Objects of this Class represent a referential constraint
 */
public class ReferentialConstraint {

  private ReferentialConstraintRole principal;
  private ReferentialConstraintRole dependent;
  private Documentation documentation;
  private Annotations annotations;

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
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
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
   * Sets the {@link Annotations} of this {@link ReferentialConstraint}
   * @param annotations
   * @return {@link ReferentialConstraint} for method chaining
   */
  public ReferentialConstraint setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}