package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.Association;

public class EdmAssociationImplProv implements EdmAssociation {

  private Association association;
  private EdmImplProv edm;

  public EdmAssociationImplProv(Association association, EdmImplProv edm) {
    this.association = association;
    this.edm = edm;
  }

  @Override
  public String getName() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getNamespace() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmTypeKind getKind() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmAssociationEnd getEnd(String role) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

}
