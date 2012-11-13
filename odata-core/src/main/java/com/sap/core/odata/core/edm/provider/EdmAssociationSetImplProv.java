package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationSet;
import com.sap.core.odata.api.edm.EdmAssociationSetEnd;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;

public class EdmAssociationSetImplProv extends EdmNamedImplProv implements EdmAssociationSet {

  private AssociationSet associationSet;
  private EdmEntityContainer edmEntityContainer;

  public EdmAssociationSetImplProv(EdmImplProv edm, AssociationSet associationSet, EdmEntityContainer edmEntityContainer) throws EdmException {
    super(edm, associationSet.getName());
    this.associationSet = associationSet;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmAssociation getAssociation() throws EdmException {
    return null;
  }

  @Override
  public EdmAssociationSetEnd getEnd(final String role) throws EdmException {
    final AssociationSetEnd end =
        associationSet.getEnd1().getRole().equals(role) ?
            associationSet.getEnd1() : associationSet.getEnd2();
    return new EdmAssociationSetEndImplProv(edm, edmEntityContainer.getEntitySet(end.getEntitySet()), end.getRole());
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return edmEntityContainer;
  }

}
