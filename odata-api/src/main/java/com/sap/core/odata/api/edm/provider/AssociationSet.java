package com.sap.core.odata.api.edm.provider;

public class AssociationSet {

  private String name;
  private FullQualifiedName association;
  private AssociationSetEnd end1;
  private AssociationSetEnd end2;
  private Documentation documentation;
  private Annotations annotations;

  public AssociationSet(String name, FullQualifiedName association, AssociationSetEnd end1, AssociationSetEnd end2, Documentation documentation, Annotations annotations) {
    this.name = name;
    this.association = association;
    this.end1 = end1;
    this.end2 = end2;
    this.documentation = documentation;
    this.annotations = annotations;
  }

  public String getName() {
    return name;
  }

  public FullQualifiedName getAssociation() {
    return association;
  }

  public AssociationSetEnd getEnd1() {
    return end1;
  }

  public AssociationSetEnd getEnd2() {
    return end2;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}