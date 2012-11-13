package com.sap.core.odata.api.edm.provider;

public class ReferentialConstraint {

  private ReferentialConstraintRole principal;
  private ReferentialConstraintRole dependent;
  private Documentation documentation;
  private Annotations annotations;

  public ReferentialConstraintRole getPrincipal() {
    return principal;
  }

  public ReferentialConstraintRole getDependent() {
    return dependent;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public ReferentialConstraint setPrincipal(ReferentialConstraintRole principal) {
    this.principal = principal;
    return this;
  }

  public ReferentialConstraint setDependent(ReferentialConstraintRole dependent) {
    this.dependent = dependent;
    return this;
  }

  public ReferentialConstraint setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public ReferentialConstraint setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}