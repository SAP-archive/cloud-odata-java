package com.sap.core.odata.api.edm.provider;

import java.util.Map;

import com.sap.core.odata.api.edm.FullQualifiedName;

public class EntityType extends ComplexType {

  private boolean hasStream;
  private CustomizableFeedMappings customizableFeedMappings;
  private Key key;
  private Map<String, NavigationProperty> navigationProperties;

  public boolean isHasStream() {
    return hasStream;
  }

  public CustomizableFeedMappings getCustomizableFeedMappings() {
    return customizableFeedMappings;
  }

  public Key getKey() {
    return key;
  }

  public Map<String, NavigationProperty> getNavigationProperties() {
    return navigationProperties;
  }

  public EntityType setHasStream(boolean hasStream) {
    this.hasStream = hasStream;
    return this;
  }

  public EntityType setCustomizableFeedMappings(CustomizableFeedMappings customizableFeedMappings) {
    this.customizableFeedMappings = customizableFeedMappings;
    return this;
  }

  public EntityType setKey(Key key) {
    this.key = key;
    return this;
  }

  public EntityType setNavigationProperties(Map<String, NavigationProperty> navigationProperties) {
    this.navigationProperties = navigationProperties;
    return this;
  }

  public EntityType setName(String name) {
    super.setName(name);
    return this;
  }

  public EntityType setBaseType(FullQualifiedName baseType) {
    super.setBaseType(baseType);
    return this;
  }

  public EntityType setAbstract(boolean isAbstract) {
    super.setAbstract(isAbstract);
    return this;
  }

  public EntityType setProperties(Map<String, Property> properties) {
    super.setProperties(properties);
    return this;
  }

  public EntityType setMapping(Mapping mapping) {
    super.setMapping(mapping);
    return this;
  }

  public EntityType setDocumentation(Documentation documentation) {
    super.setDocumentation(documentation);
    return this;
  }

  public EntityType setAnnotations(Annotations annotations) {
    super.setAnnotations(annotations);
    return this;
  }

}