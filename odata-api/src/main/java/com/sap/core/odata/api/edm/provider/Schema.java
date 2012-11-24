package com.sap.core.odata.api.edm.provider;

import java.util.Collection;
import java.util.List;

/**
 * @author SAP AG
 * <p>
 * Objects of this class represent a schema in the EDM
 */
public class Schema {

  private String namespace;
  private String alias;
  private List<Using> usings;
  private List<EntityType> entityTypes;
  private List<ComplexType> complexTypes;
  private List<Association> associations;
  private List<EntityContainer> entityContainers;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * MANDATORY
   * <p>Sets the namespace for this {@link Schema}
   * @param namespace
   * @return {@link Schema} for method chaining
   */
  public Schema setNamespace(String namespace) {
    this.namespace = namespace;
    return this;
  }

  /**
   * Sets the alias for this {@link Schema}
   * @param alias
   * @return {@link Schema} for method chaining
   */
  public Schema setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Sets the {@link Using} for this {@link Schema}
   * @param usings
   * @return {@link Schema} for method chaining
   */
  public Schema setUsings(List<Using> usings) {
    this.usings = usings;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EntityType}s for this {@link Schema}
   * @param entityTypes
   * @return {@link Schema} for method chaining
   */
  public Schema setEntityTypes(List<EntityType> entityTypes) {
    this.entityTypes = entityTypes;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link ComplexType}s for this {@link Schema}
   * @param complexTypes
   * @return {@link Schema} for method chaining
   */
  public Schema setComplexTypes(List<ComplexType> complexTypes) {
    this.complexTypes = complexTypes;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link Association}s for this {@link Schema}
   * @param associations
   * @return {@link Schema} for method chaining
   */
  public Schema setAssociations(List<Association> associations) {
    this.associations = associations;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link EntityContainer}s for this {@link Schema}
   * @param entityContainers
   * @return {@link Schema} for method chaining
   */
  public Schema setEntityContainers(List<EntityContainer> entityContainers) {
    this.entityContainers = entityContainers;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link Schema}
   * @param annotationAttributes
   * @return {@link Schema} for method chaining
   */
  public Schema setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link Schema}
   * @param annotationElements
   * @return {@link Schema} for method chaining
   */
  public Schema setAnnotationElements(Collection<AnnotationElement> annotationElements) {
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
}