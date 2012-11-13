package com.sap.core.odata.api.edm.provider;

public class Association {

  private String name;
  private AssociationEnd end1;
  private AssociationEnd end2;
  private ReferentialConstraint referentialConstraint;
  private Documentation documentation;
  private Annotations annotations;

  public String getName() {
    return name;
  }

  public AssociationEnd getEnd1() {
    return end1;
  }

  public AssociationEnd getEnd2() {
    return end2;
  }

  public ReferentialConstraint getReferentialConstraint() {
    return referentialConstraint;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public Association setName(String name) {
    this.name = name;
    return this;
  }

  public Association setEnd1(AssociationEnd end1) {
    this.end1 = end1;
    return this;
  }

  public Association setEnd2(AssociationEnd end2) {
    this.end2 = end2;
    return this;
  }

  public Association setReferentialConstraint(ReferentialConstraint referentialConstraint) {
    this.referentialConstraint = referentialConstraint;
    return this;
  }

  public Association setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public Association setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}