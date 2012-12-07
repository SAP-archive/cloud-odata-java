package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * Objects of this class represent a complex type
 * @author SAP AG
 */
public class ComplexType {

  private String name;
  private FullQualifiedName baseType;
  private boolean isAbstract;
  private Collection<Property> properties;
  private Mapping mapping;
  private Documentation documentation;
  private Collection<AnnotationAttribute> annotationAttributes;
  private Collection<AnnotationElement> annotationElements;

  /**
   * @return <b>String</b> name
   */
  public String getName() {
    return name;
  }

  /**
   * @return {@link FullQualifiedName} of the base type of this type (namespace and name) 
   */
  public FullQualifiedName getBaseType() {
    return baseType;
  }

  /**
   * @return <b>boolean</b> if this type is abstract
   */
  public boolean isAbstract() {
    return isAbstract;
  }

  /**
   * @return Collection<{@link Property}> of all properties for this type
   */
  public Collection<Property> getProperties() {
    return properties;
  }

  /**
   * @return {@link Mapping} for this type
   */
  public Mapping getMapping() {
    return mapping;
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
   * <p>Sets the name
   * @param name
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Sets the {@link FullQualifiedName} of the base type
   * @param baseType
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setBaseType(FullQualifiedName baseType) {
    this.baseType = baseType;
    return this;
  }

  /**
   * Sets if it is abstract
   * @param isAbstract
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setAbstract(boolean isAbstract) {
    this.isAbstract = isAbstract;
    return this;
  }

  /**
   * MANDATORY
   * <p>Sets the {@link Property}s
   * @param properties
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setProperties(Collection<Property> properties) {
    this.properties = properties;
    return this;
  }

  /**
   * Sets the {@link Mapping}
   * @param mapping
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setMapping(Mapping mapping) {
    this.mapping = mapping;
    return this;
  }

  /**
   * Sets the {@link Documentation}
   * @param documentation
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setDocumentation(Documentation documentation) {
    this.documentation = documentation;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link ComplexType}
   * @param annotationAttributes
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    this.annotationAttributes = annotationAttributes;
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link ComplexType}
   * @param annotationElements
   * @return {@link ComplexType} for method chaining
   */
  public ComplexType setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    this.annotationElements = annotationElements;
    return this;
  }
}