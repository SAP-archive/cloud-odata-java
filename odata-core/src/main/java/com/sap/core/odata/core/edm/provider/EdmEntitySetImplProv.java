package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.provider.EntitySet;

public class EdmEntitySetImplProv implements EdmEntitySet {

  public EdmEntitySetImplProv(EdmImplProv edm, EntitySet entitySet, EdmEntityContainer edmEntityContainer) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getName() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityType getEntityType() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntitySet getRelatedEntitySet(EdmNavigationProperty navigationProperty) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

}
