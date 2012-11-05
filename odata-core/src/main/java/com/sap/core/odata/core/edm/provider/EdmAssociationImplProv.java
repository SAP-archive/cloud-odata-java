package com.sap.core.odata.core.edm.provider;

import com.sap.core.odata.api.edm.EdmAssociation;
import com.sap.core.odata.api.edm.EdmAssociationEnd;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.provider.Association;

public class EdmAssociationImplProv extends EdmNamedImplProv implements EdmAssociation {

  private Association association;
  private String namespace;

  public EdmAssociationImplProv(EdmImplProv edm, Association association, String namespace) throws EdmException {
    super(edm, association.getName());
    this.association = association;
    this.namespace = namespace;
  }

  @Override
  public String getNamespace() throws EdmException {
    return namespace;
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
