package com.sap.core.odata.api.edm.provider;

import java.util.List;

public class EntityType extends ComplexType {

  private boolean hasStream;
  private CustomizableFeedMappings customizableFeedMappings;
  private Key key;
  private List<NavigationProperty> navigationProperties;
  
  public EntityType(String name, FullQualifiedName baseType, boolean isAbstract, List<Property> properties, Mapping mapping, Documentation documentation, Annotations annotations, boolean hasStream, CustomizableFeedMappings customizableFeedMappings, Key key, List<NavigationProperty> navigationProperties) {
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

  public List<NavigationProperty> getNavigationProperties() {
    return navigationProperties;
  }
}