package com.sap.core.odata.api.edm.provider;

public class ReferentialConstraintRole {

  private String role;
  private PropertyRef propertyRef;
  private Annotations annotations;

  public String getRole() {
    return role;
  }

  public PropertyRef getPropertyRef() {
    return propertyRef;
  }

  public Annotations getAnnotations() {
    return annotations;
  }

  public ReferentialConstraintRole setRole(String role) {
    this.role = role;
    return this;
  }

  public ReferentialConstraintRole setPropertyRef(PropertyRef propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  public ReferentialConstraintRole setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}