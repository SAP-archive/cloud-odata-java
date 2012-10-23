package com.sap.core.odata.core.experimental.edm.adapter;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;

public class EdmEntityTypeAdapter extends EdmNamedAdapter implements EdmEntityType {

  private org.odata4j.edm.EdmEntityType edmEntityType;

  public EdmEntityTypeAdapter(org.odata4j.edm.EdmEntityType edmEntityType) {
    super(edmEntityType.getName());
    this.edmEntityType = edmEntityType;
  }

  @Override
  public EdmTyped getProperty(String name) {
    org.odata4j.edm.EdmProperty edmProperty = this.edmEntityType.findProperty(name);
    if (edmProperty != null) {
      return new EdmPropertyAdapter(edmProperty);
    }
    org.odata4j.edm.EdmNavigationProperty edmNavigationProperty = this.edmEntityType.findNavigationProperty(name);
    if (edmNavigationProperty != null) {
      return new EdmNavigationPropertyAdapter(edmNavigationProperty);
    }
    return null;
  }

  @Override
  public List<String> getPropertyNames() {
    List<String> edmPropertyNames = new ArrayList<String>();
    for (org.odata4j.edm.EdmProperty edmProperty : this.edmEntityType.getProperties()) {
      edmPropertyNames.add(edmProperty.getName());
    }
    return edmPropertyNames;
  }

  @Override
  public String getNamespace() {
    return this.edmEntityType.getNamespace();
  }

  @Override
  public List<String> getKeyPropertyNames() {
    return this.edmEntityType.getKeys();
  }

  @Override
  public List<EdmProperty> getKeyProperties() {
    List<EdmProperty> edmProperties = new ArrayList<EdmProperty>();
    List<String> edmPropertyKeys = this.edmEntityType.getKeys();
    for (org.odata4j.edm.EdmProperty edmProperty : this.edmEntityType.getProperties()) {
      if (edmPropertyKeys.contains(edmProperty.getName())) {
        edmProperties.add(new EdmPropertyAdapter(edmProperty));
      }
    }
    return edmProperties;
  }

  @Override
  public boolean hasStream() {
    Boolean hasStream = this.edmEntityType.getHasStream();
    if (hasStream != null) {
      return hasStream;
    }
    return false;
  }

  @Override
  public EdmEntityType getBaseType() {
    org.odata4j.edm.EdmEntityType edmEntityType = this.edmEntityType.getBaseType();
    if (edmEntityType != null) {
      return new EdmEntityTypeAdapter(edmEntityType);
    }
    return null;
  }

  @Override
  public EdmCustomizableFeedMappings getCustomizableFeedMappings() {
    // TODO currently no support for customizable feed mappings on entity type level
    return null;
  }

  @Override
  public List<String> getNavigationPropertyNames() {
    List<String> edmNavigationPropertyNames = new ArrayList<String>();
    for (org.odata4j.edm.EdmNavigationProperty edmNavigationProperty : this.edmEntityType.getNavigationProperties()) {
      edmNavigationPropertyNames.add(edmNavigationProperty.getName());
    }
    return edmNavigationPropertyNames;
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.ENTITY;
  }

}
