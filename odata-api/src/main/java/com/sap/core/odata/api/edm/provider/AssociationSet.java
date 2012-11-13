package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.FullQualifiedName;

public class AssociationSet {

  private String name;
  private FullQualifiedName association;
  private AssociationSetEnd end1;
  private AssociationSetEnd end2;
  private Documentation documentation;
  private Annotations annotations;

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

  public AssociationSet setName(String name) {
    this.name = name;
    return this;
  }

  public AssociationSet setAssociation(FullQualifiedName association) {
    this.association = association;
    return this;
  }

  public AssociationSet setEnd1(AssociationSetEnd end1) {
    this.end1 = end1;
    return this;
  }

  public AssociationSet setEnd2(AssociationSetEnd end2) {
    this.end2 = end2;
    return this;
  }

  public AssociationSet setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public AssociationSet setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
  
  
}