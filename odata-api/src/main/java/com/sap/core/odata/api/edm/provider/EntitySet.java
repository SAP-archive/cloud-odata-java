package com.sap.core.odata.api.edm.provider;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent an entity set in the EDM
 */
public class EntitySet {

  private String name;
  private FullQualifiedName entityType;
  private Documentation documentation;
  private Annotations annotations;

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
   * @return {@link Annotations} annotations
   */
  public Annotations getAnnotations() {
    return annotations;
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
   * Sets the {@link Documentation} of this {@link EntitySet}
   * @param documentation
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the {@link Annotations} of this {@link EntitySet}
   * @param annotations
   * @return {@link EntitySet} for method chaining
   */
  public EntitySet setAnnotations(Annotations annotations) {
    this.annotations = annotations;
    return this;
  }
  
  
}