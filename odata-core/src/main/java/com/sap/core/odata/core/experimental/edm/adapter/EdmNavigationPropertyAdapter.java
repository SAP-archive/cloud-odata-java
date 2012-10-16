package com.sap.core.odata.core.experimental.edm.adapter;

import com.sap.core.odata.core.edm.EdmAssociation;
import com.sap.core.odata.core.edm.EdmMultiplicity;
import com.sap.core.odata.core.edm.EdmNavigationProperty;
import com.sap.core.odata.core.edm.EdmType;

public class EdmNavigationPropertyAdapter extends EdmNamedAdapter implements EdmNavigationProperty {

  private org.odata4j.edm.EdmNavigationProperty edmNavigationProperty;

  public EdmNavigationPropertyAdapter(org.odata4j.edm.EdmNavigationProperty edmNavigationProperty) {
    super(edmNavigationProperty.getName());
    this.edmNavigationProperty = edmNavigationProperty;
  }

  @Override
  public EdmType getType() {
    return new EdmNavigationTypeAdapter(this.edmNavigationProperty);
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    org.odata4j.edm.EdmMultiplicity edmMultiplicity = this.edmNavigationProperty.getToRole().getMultiplicity();
    if (org.odata4j.edm.EdmMultiplicity.ZERO_TO_ONE.equals(edmMultiplicity)) {
      return EdmMultiplicity.ZERO_TO_ONE;
    } else if (org.odata4j.edm.EdmMultiplicity.ONE.equals(edmMultiplicity)) {
      return EdmMultiplicity.ONE;
    } else if (org.odata4j.edm.EdmMultiplicity.MANY.equals(edmMultiplicity)) {
      return EdmMultiplicity.MANY;
    }
    return null;
  }

  @Override
  public EdmAssociation getRelationship() {
    org.odata4j.edm.EdmAssociation edmAssociation = this.edmNavigationProperty.getRelationship();
    if (edmAssociation != null) {
      return new EdmAssociationAdapter(edmAssociation);
    }
    return null;
  }

  @Override
  public String getFromRole() {
    return this.edmNavigationProperty.getFromRole().getRole();
  }

  @Override
  public String getToRole() {
    return this.edmNavigationProperty.getToRole().getRole();
  }

}
