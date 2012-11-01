package com.sap.core.odata.api.edm.provider;

public class ReferentialConstraint {

  private ReferentialConstraintRole principal;
  private ReferentialConstraintRole dependent;
  private Documentation documentation;
  private Annotations annotations;

  public ReferentialConstraint(ReferentialConstraintRole principal, ReferentialConstraintRole dependent, Documentation documentation, Annotations annotations) {
    this.principal = principal;
    this.dependent = dependent;
    this.documentation = documentation;
    this.annotations = annotations;
  }

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
}