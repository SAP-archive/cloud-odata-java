package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;

public class AssociationEnd {

  private FullQualifiedName type;
  private String role;
  private EdmMultiplicity multiplicity;
  private OnDelete onDelete;
  private Documentation documentation;
  private Annotations annotations;
  
  public AssociationEnd(FullQualifiedName type, String role, EdmMultiplicity multiplicity, OnDelete onDelete, Documentation documentation, Annotations annotations) {
    this.type = type;
    this.role = role;
    this.multiplicity = multiplicity;
    this.onDelete = onDelete;
    this.documentation = documentation;
    this.annotations = annotations;
  }

  public FullQualifiedName getType() {
    return type;
  }

  public String getRole() {
    return role;
  }

  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  public OnDelete getOnDelete() {
    return onDelete;
  }

  public Documentation getDocumentation() {
    return documentation;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}