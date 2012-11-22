package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 * <p>Objects of this Class represent a referential constraint role
 */
public class ReferentialConstraintRole {

  private String role;
  private PropertyRef propertyRef;
  private Annotations annotations;

  /**
   * @return <b>String</b> role of this {@link ReferentialConstraintRole}
   */
  public String getRole() {
    return role;
  }

  /**
   * @return {@link PropertyRef} which this {@link ReferentialConstraintRole} points to
   */
  public PropertyRef getPropertyRef() {
    return propertyRef;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p> Sets the role of this {@link ReferentialConstraintRole}
   * @param role
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setRole(String role) {
    this.role = role;
    return this;
  }

  /**
   * MANDATORY
   * <p> Sets the {@link Property} of this {@link ReferentialConstraintRole} through a {@link PropertyRef}
   * @param propertyRef
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setPropertyRef(PropertyRef propertyRef) {
    this.propertyRef = propertyRef;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link ReferentialConstraintRole}
   * @param annotations
   * @return {@link ReferentialConstraintRole} for method chaining
   */
  public ReferentialConstraintRole setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
}