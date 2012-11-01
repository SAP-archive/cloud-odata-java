package com.sap.core.odata.api.edm.provider;

public class AssociationSetEnd {

  private String role;
  private String entitySet;
  private Documentation documentation;
  private Annotations annotations;
  
  public AssociationSetEnd(String role, String entitySet, Documentation documentation, Annotations annotations) {
    this.role = role;
    this.entitySet = entitySet;
    this.documentation = documentation;
    this.annotations = annotations;
  }

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
}