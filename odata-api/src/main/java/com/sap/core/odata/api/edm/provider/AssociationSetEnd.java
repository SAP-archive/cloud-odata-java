package com.sap.core.odata.api.edm.provider;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an association set end in the EDM
 */
public class AssociationSetEnd {

  private String role;
  private String entitySet;
  private Documentation documentation;
  private Annotations annotations;

  /**
   * @return <b>String</b> role
   */
  public String getRole() {
    return role;
  }

  /**
   * @return <b>String</b> name of the target entity set
   */
  public String getEntitySet() {
    return entitySet;
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
   * <p>Sets the role of this {@link AssociationSetEnd}
   * @param role
   * @return {@link AssociatioSetnEnd} for method chaining
   */
  public AssociationSetEnd setRole(String role) {
    this.role = role;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the target entity set of this {@link AssociationSetEnd}
   * @param entitySet
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setEntitySet(String entitySet) {
    this.entitySet = entitySet;
    return this;
  }

  /**
   * Sets the {@link Documentation} of this {@link AssociationSetEnd}
   * @param documentation
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} of this {@link AssociationSetEnd}
   * @param annotations
   * @return {@link AssociationSetEnd} for method chaining
   */
  public AssociationSetEnd setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }

}