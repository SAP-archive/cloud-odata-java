package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.AssociationSet;

public class EdmAssociationSetImplProv extends EdmNamedImplProv implements EdmAssociationSet {

  private AssociationSet associationSet;
  private EdmEntityContainer edmEntityContainer;
  
  public EdmAssociationSetImplProv(EdmImplProv edm, AssociationSet associationSet, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, associationSet.getName());
    this.associationSet = associationSet;
    this.edmEntityContainer = edmEntityContainer;
    // TODO Auto-generated constructor stub
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
