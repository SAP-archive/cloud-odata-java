package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.EntitySet;

public class EdmEntitySetImplProv extends EdmNamedImplProv implements EdmEntitySet {

  private EntitySet entitySet;
  private EdmEntityContainer edmEntityContainer;
  private EdmEntityType edmEntityType;

  public EdmEntitySetImplProv(EdmImplProv edm, EntitySet entitySet, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, entitySet.getName());
    this.entitySet = entitySet;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmEntityType getEntityType() throws EdmException {
    if (edmEntityType == null) {
      FullQualifiedName fqName = entitySet.getEntityType();
      edmEntityType = edm.getEntityType(fqName.getNamespace(), fqName.getName());
      if (edmEntityType == null) {
        throw new EdmException();
      }
    }
    return edmEntityType;
  }

  @Override
  public EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }
}