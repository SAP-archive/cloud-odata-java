package com.sap.core.odata.api.edm.provider;

import java.util.Collection;

import com.sap.core.odata.api.edm.FullQualifiedName;

/**
 * @author SAP AG
 *
 */
public class EntityType extends ComplexType {

  private boolean hasStream;
  private CustomizableFeedMappings customizableFeedMappings;
  private Key key;
  private Collection<NavigationProperty> navigationProperties;

  /**
   * @return <b>boolean</b> if this EntityType is a media resource
   */
  public boolean isHasStream() {
    return hasStream;
  }

  /**
   * @return {@link CustomizableFeedMappings} of this entity type
   */
  public CustomizableFeedMappings getCustomizableFeedMappings() {
    return customizableFeedMappings;
  }

  /**
   * @return {@link Key} of this entity type
   */
  public Key getKey() {
    return key;
  }

  /**
   * @return Collection<{@link NavigationProperty}> of this entity type
   */
  public Collection<NavigationProperty> getNavigationProperties() {
    return navigationProperties;
  }

  /**
   * Sets if this {@link EntityType} is a media resource
   * @param hasStream
   * @return {@link EntityType} for method chaining,
   */
  public EntityType setHasStream(boolean hasStream) {
    this.hasStream = hasStream;
    return this;
  }

  /**
   * Sets the {@link CustomizableFeedMappings} for this {@link EntityType}
   * @param customizableFeedMappings
   * @return {@link EntityType} for method chaining
   */
  public EntityType setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    this.customizableFeedMappings = customizableFeedMappings;
    return this;
  }

  /**
   * Sets the {@link Key} for this {@link EntityType}
   * @param key
   * @return {@link EntityType} for method chaining
   */
  public EntityType setKey(Key key) {
    this.key = key;
    return this;
  }

  /**
   * Sets the {@link NavigationProperty}s for this {@link EntityType}
   * @param navigationProperties
   * @return {@link EntityType} for method chaining
   */
  public EntityType setNavigationProperties(Collection<NavigationProperty> navigationProperties) {
    this.navigationProperties = navigationProperties;
    return this;
  }

  /**
   * @param
   * @return {@link EntityType} for method chaining
   */
  public EntityType setName(String name) {
    super.setName(name);
    return this;
  }

  /**
   * @param
   * @return {@link EntityType} for method chaining
   */
  public EntityType setBaseType(FullQualifiedName baseType) {
    super.setBaseType(baseType);
    return this;
  }

  /**
   * @param
   * @return {@link EntityType} for method chaining
   */
  public EntityType setAbstract(boolean isAbstract) {
    super.setAbstract(isAbstract);
    return this;
  }

  /**
   * @param
   * @return {@link EntityType} for method chaining
   */
  public EntityType setProperties(Collection<Property> properties) {
    super.setProperties(properties);
    return this;
  }

  /**
   * @param
   * @return {@link EntityType} for method chaining
   */
  public EntityType setMapping(Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  /**
   * @param
   * @return {@link EntityType} for method chaining
   */
  public EntityType setDocumentation(Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationAttribute} for this {@link EntityType}
   * @param annotationAttributes
   * @return {@link EntityType} for method chaining
   */
  public EntityType setAnnotationAttributes(Collection<AnnotationAttribute> annotationAttributes) {
    super.setAnnotationAttributes(annotationAttributes);
    return this;
  }

  /**
   * Sets the collection of {@link AnnotationElement} for this {@link EntityType}
   * @param annotationElements
   * @return {@link EntityType} for method chaining
   */
  public EntityType setAnnotationElements(Collection<AnnotationElement> annotationElements) {
    super.setAnnotationElements(annotationElements);
    return this;
  }
}