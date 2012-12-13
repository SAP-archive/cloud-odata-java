package com.sap.core.odata.core.edm.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmAnnotations;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.PropertyRef;

public class EdmEntityTypeImplProv extends EdmStructuralTypeImplProv implements EdmEntityType {

  private EntityType entityType;

  private Map<String, EdmProperty> keyProperties;
  private List<EdmProperty> edmKeyProperties;
  private List<String> edmKeyPropertyNames;

  private Map<String, NavigationProperty> navigationProperties;
  private List<String> edmNavigationPropertyNames;

  public EdmEntityTypeImplProv(EdmImplProv edm, EntityType entityType, String namespace) throws EdmException {
    super(edm, (ComplexType) entityType, EdmTypeKind.ENTITY, namespace);
    this.entityType = entityType;

    buildNavigationPropertiesInternal();
  }

  private void buildNavigationPropertiesInternal() throws EdmException {
    navigationProperties = new HashMap<String, NavigationProperty>();

    if (entityType.getNavigationProperties() != null)
      for (final NavigationProperty navigationProperty : entityType.getNavigationProperties())
        navigationProperties.put(navigationProperty.getName(), navigationProperty);
  }

  @Override
  public List<String> getKeyPropertyNames() throws EdmException {
    if (edmKeyPropertyNames == null) {
      if (edmBaseType != null) {
        return ((EdmEntityType) edmBaseType).getKeyPropertyNames();
      }

      edmKeyPropertyNames = new ArrayList<String>();

      if (entityType.getKey() != null)
        for (final PropertyRef keyProperty : entityType.getKey().getKeys())
          edmKeyPropertyNames.add(keyProperty.getName());
      else
        //Entity Type does not define a key
        throw new EdmException(EdmException.COMMON);
    }

    return edmKeyPropertyNames;
  }

  @Override
  public List<EdmProperty> getKeyProperties() throws EdmException {
    if (edmKeyProperties == null) {
      if (edmBaseType != null) {
        return ((EdmEntityType) edmBaseType).getKeyProperties();
      }

      if (keyProperties == null) {
        keyProperties = new HashMap<String, EdmProperty>();
        edmKeyProperties = new ArrayList<EdmProperty>();

        for (String keyPropertyName : getKeyPropertyNames()) {
          final EdmTyped edmProperty = (EdmProperty) getProperty(keyPropertyName);
          if (edmProperty != null && edmProperty instanceof EdmProperty) {
            keyProperties.put(keyPropertyName, (EdmProperty) edmProperty);
            edmKeyProperties.add((EdmProperty) edmProperty);
          } else {
            throw new EdmException(EdmException.COMMON);
          }
        }
      }
    }

    return edmKeyProperties;
  }

  @Override
  public boolean hasStream() throws EdmException {
    return entityType.isHasStream();
  }

  @Override
  public EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException {
    return entityType.getCustomizableFeedMappings();
  }

  @Override
  public List<String> getNavigationPropertyNames() throws EdmException {
    if (edmNavigationPropertyNames == null) {
      edmNavigationPropertyNames = new ArrayList<String>();
      if (edmBaseType != null)
        edmNavigationPropertyNames.addAll(((EdmEntityType) edmBaseType).getNavigationPropertyNames());
      if (entityType.getNavigationProperties() != null)
        for (final NavigationProperty navigationProperty : entityType.getNavigationProperties())
          edmNavigationPropertyNames.add(navigationProperty.getName());
    }
    return edmNavigationPropertyNames;
  }

  @Override
  public EdmEntityType getBaseType() throws EdmException {
    return (EdmEntityType) edmBaseType;
  }

  @Override
  protected EdmTyped getPropertyInternal(String name) throws EdmException {
    EdmTyped edmProperty = super.getPropertyInternal(name);

    if (edmProperty != null) {
      return edmProperty;
    }

    if (navigationProperties.containsKey(name)) {
      edmProperty = createNavigationProperty(navigationProperties.get(name));
      edmProperties.put(name, edmProperty);
    } else if (edmBaseType != null) {
      edmProperty = edmBaseType.getProperty(name);
      if (edmProperty != null) {
        edmProperties.put(name, edmProperty);
      }
    }

    return edmProperty;
  }

  protected EdmTyped createNavigationProperty(NavigationProperty property) throws EdmException {
    return new EdmNavigationPropertyImplProv(edm, property);
  }

  @Override
  public EdmAnnotations getAnnotations() throws EdmException {
    return new EdmAnnotationsImplProv(entityType.getAnnotationAttributes(), entityType.getAnnotationElements());
  }
}