package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmAssociationEnd;

import com.sap.core.odata.core.edm.EdmEnd;
import com.sap.core.odata.core.edm.EdmEntityType;
import com.sap.core.odata.core.edm.EdmMultiplicity;

public class EdmAssociationEndAdapter implements EdmEnd {

  private EdmAssociationEnd edmAssociationEnd;

  public EdmAssociationEndAdapter(EdmAssociationEnd edmAssociationEnd) {
    this.edmAssociationEnd = edmAssociationEnd;
  }

  @Override
  public String getRole() {
    return this.edmAssociationEnd.getRole();
  }

  @Override
  public EdmEntityType getType() {
    org.odata4j.edm.EdmEntityType edmEntityType = this.edmAssociationEnd.getType();
    if (edmEntityType != null) {
      return new EdmEntityTypeAdapter(edmEntityType);
    }
    return null;
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    org.odata4j.edm.EdmMultiplicity edmMultiplicity = this.edmAssociationEnd.getMultiplicity();
    if (edmMultiplicity.equals(org.odata4j.edm.EdmMultiplicity.ZERO_TO_ONE)) {
      return EdmMultiplicity.ZERO_TO_ONE;
    } else if (edmMultiplicity.equals(org.odata4j.edm.EdmMultiplicity.ONE)) {
      return EdmMultiplicity.ONE;
    } else if (edmMultiplicity.equals(org.odata4j.edm.EdmMultiplicity.MANY)) {
      return EdmMultiplicity.MANY;
    }
    return null;
  }

}
