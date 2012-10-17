package com.sap.core.odata.core.experimental.edm.adapter;

import com.sap.core.odata.core.edm.EdmAssociation;
import com.sap.core.odata.core.edm.EdmEnd;
import com.sap.core.odata.core.edm.EdmException;
import com.sap.core.odata.core.edm.EdmTypeEnum;

public class EdmAssociationAdapter extends EdmNamedAdapter implements EdmAssociation {

  private org.odata4j.edm.EdmAssociation edmAssociation;

  public EdmAssociationAdapter(org.odata4j.edm.EdmAssociation edmAssociation) {
    super(edmAssociation.getName());
    this.edmAssociation = edmAssociation;
  }

  @Override
  public String getNamespace() throws EdmException {
    return this.edmAssociation.getNamespace();
  }

  @Override
  public EdmEnd getEnd(String role) throws EdmException {
    if (role.equals(this.edmAssociation.getEnd1().getRole())) {
      return new EdmAssociationEndAdapter(this.edmAssociation.getEnd1());
    } else if (role.equals(this.edmAssociation.getEnd2().getRole())) {
      return new EdmAssociationEndAdapter(this.edmAssociation.getEnd2());
    }
    return null;
  }

  @Override
  public EdmTypeEnum getKind() {
    return EdmTypeEnum.ASSOCIATION;
  }
}
