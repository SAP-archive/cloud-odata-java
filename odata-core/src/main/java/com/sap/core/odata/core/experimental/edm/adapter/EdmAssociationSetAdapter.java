package com.sap.core.odata.core.experimental.edm.adapter;

import com.sap.core.odata.core.edm.EdmAssociation;
import com.sap.core.odata.core.edm.EdmAssociationSet;
import com.sap.core.odata.core.edm.EdmEnd;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmException;

public class EdmAssociationSetAdapter extends EdmNamedAdapter implements EdmAssociationSet {

  private org.odata4j.edm.EdmAssociationSet edmAssociationSet;
  private org.odata4j.edm.EdmEntityContainer edmEntityContainer;

  public EdmAssociationSetAdapter(org.odata4j.edm.EdmAssociationSet edmAssociationSet, org.odata4j.edm.EdmEntityContainer edmEntityContainer) {
    super(edmAssociationSet.getName());
    this.edmAssociationSet = edmAssociationSet;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public EdmAssociation getAssociation() throws EdmException {
    return new EdmAssociationAdapter(this.edmAssociationSet.getAssociation());
  }

  @Override
  public EdmEnd getEnd(String role) throws EdmException {
    if (role.equals(this.edmAssociationSet.getEnd1().getRole())) {
      new EdmAssociationSetEndAdapter(this.edmAssociationSet.getEnd1(), this.edmEntityContainer);
    } else if (role.equals(this.edmAssociationSet.getEnd2().getRole())) {
      new EdmAssociationSetEndAdapter(this.edmAssociationSet.getEnd2(), this.edmEntityContainer);
    }
    return null;
  }

  @Override
  public EdmEntityContainer getEntityContainer() throws EdmException {
    return new EdmEntityContainerAdapter(this.edmEntityContainer);
  }
}
