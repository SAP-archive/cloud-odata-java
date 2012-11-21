package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 *
 */
public class ReferentialConstraintRole {

  private String role;
  private PropertyRef propertyRef;
  private Annotations annotations;

  /**
   * @return
   */
  public String getRole() {
    return role;
  }

  /**
   * @return
   */
  public PropertyRef getPropertyRef() {
    return propertyRef;
  }

  /**
   * @return
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * @param role
   * @return
   */
  public ReferentialConstraintRole setRole(String role) {
    this.role = role;
    return this;
  }

  /**
   * @param propertyRef
   * @return
   */
  public ReferentialConstraintRole setPropertyRef(PropertyRef propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  /**
   * @param annotations
   * @return
   */
  public ReferentialConstraintRole setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}