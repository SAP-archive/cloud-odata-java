package com.sap.core.odata.core.edm.provider;

import java.util.Collection;
import java.util.List;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.NavigationProperty;

public class EdmNavigationPropertyImplProv extends EdmNamedImplProv implements EdmNavigationProperty {

  private NavigationProperty navigationProperty;

  public EdmNavigationPropertyImplProv(EdmImplProv edm, NavigationProperty property) throws EdmException {
    super(edm, property.getName());
    navigationProperty = property;
  }

  @Override
  public EdmType getType() throws EdmException {
    final EdmEntityType entityType = getRelationship().getEnd(navigationProperty.getToRole()).getEntityType();
    return new EdmEntityType() {

      @Override
      public EdmTyped getProperty(String name) throws EdmException {
        return entityType.getProperty(name);
      }

      @Override
      public Collection<String> getPropertyNames() throws EdmException {
        return entityType.getPropertyNames();
      }

      @Override
      public String getNamespace() throws EdmException {
        return entityType.getNamespace();
      }

      @Override
      public EdmTypeKind getKind() {
        return EdmTypeKind.NAVIGATION;
      }

      @Override
      public String getName() throws EdmException {
        return entityType.getName();
      }

      @Override
      public Collection<String> getKeyPropertyNames() throws EdmException {
        return entityType.getKeyPropertyNames();
      }

      @Override
      public List<EdmProperty> getKeyProperties() throws EdmException {
        return entityType.getKeyProperties();
      }

      @Override
      public boolean hasStream() throws EdmException {
        return entityType.hasStream();
      }

      @Override
      public EdmEntityType getBaseType() throws EdmException {
        return entityType.getBaseType();
      }

      @Override
      public EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException {
        return entityType.getCustomizableFeedMappings();
      }

      @Override
      public Collection<String> getNavigationPropertyNames() throws EdmException {
        return entityType.getNavigationPropertyNames();
      }
    };
  }

  @Override
  public EdmMultiplicity getMultiplicity() throws EdmException {
    return getRelationship().getEnd(navigationProperty.getToRole()).getMultiplicity();
  }

  @Override
  public EdmAssociation getRelationship() throws EdmException {
    final FullQualifiedName relationship = navigationProperty.getRelationship();
    return edm.getAssociation(relationship.getNamespace(), relationship.getName());
  }

  @Override
  public String getFromRole() throws EdmException {
    return navigationProperty.getFromRole();
  }

  @Override
  public String getToRole() throws EdmException {
    return navigationProperty.getToRole();
  }

}
