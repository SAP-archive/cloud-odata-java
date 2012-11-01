package com.sap.core.odata.api.edm.provider;

public class ReferentialConstraintRole {

  private String role;
  private PropertyRef propertyRef;
  private Annotations annotations;

  public ReferentialConstraintRole(String role, PropertyRef propertyRef, Annotations annotations) {
    this.role = role;
    this.propertyRef = propertyRef;
    this.annotations = annotations;
  }

  public String getRole() {
    return role;
  }

  public PropertyRef getPropertyRef() {
    return propertyRef;
  }

  public Annotations getAnnotations() {
    return annotations;
  }
}