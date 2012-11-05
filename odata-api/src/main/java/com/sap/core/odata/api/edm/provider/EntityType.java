package com.sap.core.odata.api.edm.provider;

import java.util.Map;

import com.sap.core.odata.api.edm.FullQualifiedName;

public class EntityType extends ComplexType {

  private boolean hasStream;
  private CustomizableFeedMappings customizableFeedMappings;
  private Key key;
  private Map<String, NavigationProperty> navigationProperties;

  public EntityType(String name, FullQualifiedName baseType, boolean isAbstract, Map<String, Property> properties, Mapping mapping, Documentation documentation, Annotations annotations, boolean hasStream, CustomizableFeedMappings customizableFeedMappings, Key key, Map<String, NavigationProperty> navigationProperties) {
    super(name, baseType, isAbstract, properties, mapping, documentation, annotations);
    this.hasStream = hasStream;
    this.customizableFeedMappings = customizableFeedMappings;
    this.key = key;
    this.navigationProperties = navigationProperties;
  }

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
}