package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;

public class EdmAssociationSetEndImplProv implements EdmAssociationSetEnd {

  private EdmImplProv edm;
  private EdmEntitySet entitySet;
  private String role;

  public EdmAssociationSetEndImplProv(EdmImplProv edm, EdmEntitySet entitySet, String role) throws EdmException {
    this.edm = edm;
    this.entitySet = entitySet;
    this.role = role;
  }

  @Override
  public EdmEntitySet getEntitySet() throws EdmException {
    return entitySet;
  }

  @Override
  public String getRole() {
    return role;
  }
}
