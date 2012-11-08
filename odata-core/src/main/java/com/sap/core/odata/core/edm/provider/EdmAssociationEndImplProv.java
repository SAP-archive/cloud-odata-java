package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.AssociationEnd;

public class EdmAssociationEndImplProv implements EdmAssociationEnd {

  private EdmImplProv edm;
  private AssociationEnd associationEnd;

  public EdmAssociationEndImplProv(EdmImplProv edm, AssociationEnd associationEnd) throws EdmException {
    this.edm = edm;
    this.associationEnd = associationEnd;
  }

  @Override
  public String getRole() {
    return associationEnd.getRole();
  }

  @Override
  public EdmEntityType getEntityType() throws EdmException {
    final FullQualifiedName type = associationEnd.getType();
    return edm.getEntityType(type.getNamespace(), type.getName());
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    return associationEnd.getMultiplicity();
  }

}
