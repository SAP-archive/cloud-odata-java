package com.sap.core.odata.api.edm.provider;

public class AssociationSetEnd {

  private String role;
  private String entitySet;
  private Documentation documentation;
  private Annotations annotations;

  public String getRole() {
    return role;
  }

  public String getEntitySet() {
    return entitySet;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public AssociationSetEnd setRole(String role) {
    this.role = role;
    return this;
  }

  public AssociationSetEnd setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  public AssociationSetEnd setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public AssociationSetEnd setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}