package com.sap.core.odata.api.edm.provider;

import java.util.List;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent an association end
 * @author SAP AG
 */
public class AssociationEnd {

  private FullQualifiedName type;
  private String role;
  private EdmMultiplicity multiplicity;
  private OnDelete onDelete;
  private Documentation documentation;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

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
   * @return collection of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * Sets the {@link FullQualifiedName} for this {@link AssociationEnd}
   * @param type
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setType(final FullQualifiedName type) {
    this.type = type;
    return this;
  }

  /**
   * Sets the role for this {@link AssociationEnd}
   * @param role
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setRole(final String role) {
    this.role = role;
    return this;
  }

  /**
   * Sets the {@link EdmMultiplicity} for this {@link AssociationEnd}
   * @param multiplicity
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setMultiplicity(final EdmMultiplicity multiplicity) {
    this.multiplicity = multiplicity;
    return this;
  }

  /**
   * Sets {@link OnDelete} for this {@link AssociationEnd}
   * @param onDelete
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setOnDelete(final OnDelete onDelete) {
    this.onDelete = onDelete;
    return this;
  }

  /**
   * Sets the {@link Documentation} for this {@link AssociationEnd}
   * @param documentation
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setDocumentation(final Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link AssociationEnd}
   * @param annotationAttributes
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link AssociationEnd}
   * @param annotationElements
   * @return {@link AssociationEnd} for method chaining
   */
  public AssociationEnd setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}