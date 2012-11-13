package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

public class AssociationEnd {

  private FullQualifiedName type;
  private String role;
  private EdmMultiplicity multiplicity;
  private OnDelete onDelete;
  private Documentation documentation;
  private Annotations annotations;

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

  public AssociationEnd setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  public AssociationEnd setRole(String role) {
    this.role = role;
    return this;
  }

  public AssociationEnd setMultiplicity(EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  public AssociationEnd setOnDelete(OnDelete onDelete) {
    this.onDelete = onDelete;
    return this;
  }

  public AssociationEnd setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  public AssociationEnd setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}