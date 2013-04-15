/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.edm.provider;

import java.util.List;

/**
 * Objects of this class represent a schema
 * @author SAP AG
 */
public class Schema {

  private String namespace;
  private String alias;
  private List<Using> usings;
  private List<EntityType> entityTypes;
  private List<ComplexType> complexTypes;
  private List<Association> associations;
  private List<EntityContainer> entityContainers;
  private List<AnnotationAttribute> annotationAttributes;
  private List<AnnotationElement> annotationElements;

  /**
   * Sets the namespace for this {@link Schema}
   * @param namespace
   * @return {@link Schema} for method chaining
   */
  public Schema setNamespace(final String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the alias for this {@link Schema}
   * @param alias
   * @return {@link Schema} for method chaining
   */
  public Schema setAlias(final String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Sets the {@link Using} for this {@link Schema}
   * @param usings
   * @return {@link Schema} for method chaining
   */
  public Schema setUsings(final List<Using> usings) {
    this.usings = usings;
    return this;
  }

  /**
   * Sets the {@link EntityType}s for this {@link Schema}
   * @param entityTypes
   * @return {@link Schema} for method chaining
   */
  public Schema setEntityTypes(final List<EntityType> entityTypes) {
    this.entityTypes = entityTypes;
    return this;
  }

  /**
   * Sets the {@link ComplexType}s for this {@link Schema}
   * @param complexTypes
   * @return {@link Schema} for method chaining
   */
  public Schema setComplexTypes(final List<ComplexType> complexTypes) {
    this.complexTypes = complexTypes;
    return this;
  }

  /**
   * Sets the {@link Association}s for this {@link Schema}
   * @param associations
   * @return {@link Schema} for method chaining
   */
  public Schema setAssociations(final List<Association> associations) {
    this.associations = associations;
    return this;
  }

  /**
   * Sets the {@link EntityContainer}s for this {@link Schema}
   * @param entityContainers
   * @return {@link Schema} for method chaining
   */
  public Schema setEntityContainers(final List<EntityContainer> entityContainers) {
    this.entityContainers = entityContainers;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationAttribute} for this {@link Schema}
   * @param annotationAttributes
   * @return {@link Schema} for method chaining
   */
  public Schema setAnnotationAttributes(final List<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the List of {@link AnnotationElement} for this {@link Schema}
   * @param annotationElements
   * @return {@link Schema} for method chaining
   */
  public Schema setAnnotationElements(final List<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }

  /**
   * @return <b>String</b> namespace of this {@link Schema}
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * @return <b>String</b> alias of this {@link Schema}
   */
  public String getAlias() {
    return alias;
  }

  /**
   * @return List<{@link Using}> of this {@link Schema}
   */
  public List<Using> getUsings() {
    return usings;
  }

  /**
   * @return List<{@link EntityType}> of this {@link Schema}
   */
  public List<EntityType> getEntityTypes() {
    return entityTypes;
  }

  /**
   * @return List<{@link ComplexType}> of this {@link Schema}
   */
  public List<ComplexType> getComplexTypes() {
    return complexTypes;
  }

  /**
   * @return List<{@link Association}> of this {@link Schema}
   */
  public List<Association> getAssociations() {
    return associations;
  }

  /**
   * @return List<{@link EntityContainer}> of this {@link Schema}
   */
  public List<EntityContainer> getEntityContainers() {
    return entityContainers;
  }

  /**
   * @return List of {@link AnnotationAttribute} annotation attributes
   */
  public List<AnnotationAttribute> getAnnotationAttributes() {
    return annotationAttributes;
  }

  /**
   * @return List of {@link AnnotationElement} annotation elements
   */
  public List<AnnotationElement> getAnnotationElements() {
    return annotationElements;
  }
}