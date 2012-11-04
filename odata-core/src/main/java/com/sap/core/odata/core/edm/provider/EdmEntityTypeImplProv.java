package com.sap.core.odata.core.edm.provider;

import java.util.Collection;
import java.util.List;

import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.provider.EntityType;

public class EdmEntityTypeImplProv extends EdmStructuralTypeImplProv implements EdmEntityType {

  private EntityType entityType;
  private EdmImplProv edm;

  public EdmEntityTypeImplProv(EntityType entityType, EdmImplProv edm) {
    this.entityType = entityType;
    this.edm = edm;
  }

  @Override
  public Collection<String> getKeyPropertyNames() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<EdmProperty> getKeyProperties() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean hasStream() throws EdmException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public EdmCustomizableFeedMappings getCustomizableFeedMappings() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<String> getNavigationPropertyNames() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }
}