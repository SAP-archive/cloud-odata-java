package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an association end in the EDM
 */
public class AssociationEnd {

  private FullQualifiedName type;
  private String role;
  private EdmMultiplicity multiplicity;
  private OnDelete onDelete;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return {@link FullQualifiedName} full qualified name  (namespace and name)
   */
  public FullQualifiedName getType() {
    return type;
  }

  /**
   * @return <b>String</b> role
   */
  public String getRole() {
    return role;
  }

  /**
   * @return {@link EdmMultiplicity} multiplicity of this end
   */
  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  /**
   * @return {@link OnDelete} on delete
   */
  public OnDelete getOnDelete() {
    return onDelete;
  }

  /**
   * @return {@link Documentation} documentation
   */
  public Documentation getDocumentation() {
    return documentation;
  }

  /**
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} for this {@link AssociationEnd}
   * @param type
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setType(FullQualifiedName type) {
    this.type = type;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the role for this {@link AssociationEnd}
   * @param role
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setRole(String role) {
    this.role = role;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EdmMultiplicity} for this {@link AssociationEnd}
   * @param multiplicity
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setMultiplicity(EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  /**
   * Sets {@link OnDelete} for this {@link AssociationEnd}
   * @param onDelete
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setOnDelete(OnDelete onDelete) {
    this.onDelete = onDelete;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link AssociationEnd}
   * @param documentation
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} for this {@link AssociationEnd}
   * @param annotations
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}