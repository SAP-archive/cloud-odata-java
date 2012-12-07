package com.sap.core.odata.core.edm.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    this.navigationProperties = new HashMap<String, NavigationProperty>();

    List<NavigationProperty> navigationProperties = entityType.getNavigationProperties();
    if (navigationProperties != null) {
      NavigationProperty navigationProperty;
      for (Iterator<NavigationProperty> iterator = navigationProperties.iterator(); iterator.hasNext();) {
        navigationProperty = iterator.next();
        this.navigationProperties.put(navigationProperty.getName(), navigationProperty);
      }
    }
  }

  @Override
  public List<String> getKeyPropertyNames() throws EdmException {
    if (edmKeyPropertyNames == null) {
      if (edmBaseType != null) {
        return ((EdmEntityType) edmBaseType).getKeyPropertyNames();
      }

      edmKeyPropertyNames = new ArrayList<String>();

      if (entityType.getKey() != null) {
        for (Iterator<PropertyRef> iterator = entityType.getKey().getKeys().iterator(); iterator.hasNext();) {
          edmKeyPropertyNames.add(iterator.next().getName());
        }
      } else {
        //Entity Type does not define a key
        throw new EdmException(EdmException.COMMON);
      }
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
        EdmProperty edmProperty;
        for (String keyPropertyName : getKeyPropertyNames()) {
          try {
            edmProperty = (EdmProperty) getProperty(keyPropertyName);
          } catch (ClassCastException e) {
            throw new EdmException(EdmException.COMMON, e);
          }
          if (edmProperty != null) {
            keyProperties.put(keyPropertyName, edmProperty);
          } else {
            throw new EdmException(EdmException.COMMON);
          }

        }
        edmKeyProperties = new ArrayList<EdmProperty>();
        edmKeyProperties.addAll(keyProperties.values());

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
      edmNavigationPropertyNames.addAll(navigationProperties.keySet());
      if (edmBaseType != null) {
        edmNavigationPropertyNames.addAll(((EdmEntityType) edmBaseType).getNavigationPropertyNames());
      }
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