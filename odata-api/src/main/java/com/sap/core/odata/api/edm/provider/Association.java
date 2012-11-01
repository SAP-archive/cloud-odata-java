package com.sap.core.odata.api.edm.provider;

public class Association {

  private String name;
  private AssociationEnd end1;
  private AssociationEnd end2;
  private ReferentialConstraint referentialConstraint;
  private Documentation documentation;
  private Annotations annotations;
  
  public Association(String name, AssociationEnd end1, AssociationEnd end2, ReferentialConstraint referentialConstraint, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.end1 = end1;
    this.end2 = end2;
    this.referentialConstraint = referentialConstraint;
    this.documentation = documentation;
    this.annotations = annotations;
  }

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
}