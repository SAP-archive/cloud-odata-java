package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 *
 */
public class ReferentialConstraint {

  private ReferentialConstraintRole principal;
  private ReferentialConstraintRole dependent;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return
   */
  public ReferentialConstraintRole getPrincipal() {
    return principal;
  }

  /**
   * @return
   */
  public ReferentialConstraintRole getDependent() {
    return dependent;
  }

  /**
   * @return
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * @param principal
   * @return
   */
  public ReferentialConstraint setPrincipal(ReferentialConstraintRole principal) {
    this.principal = principal;
    return this;
  }

  /**
   * @param dependent
   * @return
   */
  public ReferentialConstraint setDependent(ReferentialConstraintRole dependent) {
    this.dependent = dependent;
    return this;
  }

  /**
   * @param documentation
   * @return
   */
  public ReferentialConstraint setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * @param annotations
   * @return
   */
  public ReferentialConstraint setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}