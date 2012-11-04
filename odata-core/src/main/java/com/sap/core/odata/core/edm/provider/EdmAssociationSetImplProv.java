package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.AssociationSet;

public class EdmAssociationSetImplProv implements EdmAssociationSet {

  public EdmAssociationSetImplProv(EdmImplProv edm, AssociationSet associationSet, EdmEntityContainer edmEntityContainer) {
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getName() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmAssociation getAssociation() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmAssociationSetEnd getEnd(String role) throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    // TODO Auto-generated method stub
    return null;
  }

}
