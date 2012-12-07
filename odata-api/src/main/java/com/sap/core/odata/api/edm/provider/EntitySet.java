package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent an entity set
 * @author SAP AG
 */
public class EntitySet {

  private String name;
  private FullQualifiedName entityType;
  private Documentation documentation;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * @return <b>String> name of this entity set
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} of the entity type of this entity set
   */
  public FullQualifiedName getEntityType() {
    return entityType;
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
  public Collection<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return collection of {@link AnnotationElement} annotation elements
   */
  public Collection<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }

  /**
   * MANDATORY
   * <p>Sets the name of this {@link EntitySet}
   * @param name
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link FullQualifiedName} of the {@link EntityType} of this {@link EntitySet}
   * @param entityType
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setEntityType(FullQualifiedName entityType) {
    this.entityType = entityType;
    return this;
  }

  /**
   * Sets the {@link Documentation}
   * @param documentation
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link EntitySet}
   * @param annotationAttributes
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link EntitySet}
   * @param annotationElements
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}