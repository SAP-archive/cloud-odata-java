package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmAssociationSetEnd;
import org.odata4j.edm.EdmEntityContainer;

import com.sap.core.odata.api.edm.EdmEntitySet;

public class EdmAssociationSetEndAdapter implements com.sap.core.odata.api.edm.EdmAssociationSetEnd {

  private EdmAssociationSetEnd edmAssociationSetEnd;
  private EdmEntityContainer edmEntityContainer;

  public EdmAssociationSetEndAdapter(EdmAssociationSetEnd edmAssociationSetEnd, EdmEntityContainer edmEntityContainer) {
    this.edmAssociationSetEnd = edmAssociationSetEnd;
    this.edmEntityContainer = edmEntityContainer;
  }

  @Override
  public String getRole() {
    return this.edmAssociationSetEnd.getRole().getRole();
  }

  @Override
  public EdmEntitySet getEntitySet() {
    org.odata4j.edm.EdmEntitySet edmEntitySet = this.edmAssociationSetEnd.getEntitySet();
    if (edmEntitySet != null) {
      return new EdmEntitySetAdapter(edmEntitySet, this.edmEntityContainer);
    }
    return null;
  }
}
