package com.sap.core.odata.core.uri;

import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

public class NavigationSegment {

  private EdmNavigationProperty navigationProperty;
  private EdmEntitySet targetEntitySet;
  private List<KeyPredicate> keyPredicates = Collections.emptyList();

  public List<KeyPredicate> getKeyPredicates() {
    return keyPredicates;
  }

  public void setKeyPredicates(List<KeyPredicate> keyPredicates) {
    this.keyPredicates = keyPredicates;
  }

  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  public EdmEntitySet getEntitySet() {
    return targetEntitySet;
  }

  public void setNavigationProperty(EdmNavigationProperty edmNavigationProperty) {
    this.navigationProperty = edmNavigationProperty;
  }

  public void setEntitySet(EdmEntitySet edmEntitySet) {
    this.targetEntitySet = edmEntitySet;
  }

  @Override
  public String toString() {
    return "NavigationProperty:" + navigationProperty + " Target Entity Set: " + targetEntitySet;
  }

}
