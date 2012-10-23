package com.sap.core.odata.core.experimental.edm.adapter;

import org.odata4j.edm.EdmNavigationProperty;

import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;

public class EdmNavigationTypeAdapter implements EdmType, EdmTyped {

  private EdmNavigationProperty edmNavigationProperty;
  
  public EdmNavigationTypeAdapter(EdmNavigationProperty edmNavigationProperty) {
    this.edmNavigationProperty = edmNavigationProperty;
  }
  
  @Override
  public String getName() {
    return edmNavigationProperty.getName();
  }

  @Override
  public String getNamespace() {
    return edmNavigationProperty.getToRole().getType().getName();
  }

  @Override
  public EdmTypeKind getKind() {
    return EdmTypeKind.NAVIGATION;
  }

  @Override
  public EdmType getType() {
    return new EdmEntityTypeAdapter(edmNavigationProperty.getToRole().getType());
  }

  @Override
  public EdmMultiplicity getMultiplicity() {
    org.odata4j.edm.EdmMultiplicity edmMultiplicity = edmNavigationProperty.getToRole().getMultiplicity();
    if (org.odata4j.edm.EdmMultiplicity.ZERO_TO_ONE.equals(edmMultiplicity)) {
      return EdmMultiplicity.ZERO_TO_ONE;
    } else if (org.odata4j.edm.EdmMultiplicity.ONE.equals(edmMultiplicity)) {
      return EdmMultiplicity.ONE;
    } else if (org.odata4j.edm.EdmMultiplicity.MANY.equals(edmMultiplicity)) {
      return EdmMultiplicity.MANY;
    }
    return null;
  }
}
