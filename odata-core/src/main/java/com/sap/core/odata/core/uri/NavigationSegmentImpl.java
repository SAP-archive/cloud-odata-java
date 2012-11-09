package com.sap.core.odata.core.uri;

import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;

/**
 * @author SAP AG
 */
public class NavigationSegmentImpl implements NavigationSegment {

  private EdmNavigationProperty navigationProperty;
  private EdmEntitySet targetEntitySet;
  private List<KeyPredicate> keyPredicates = Collections.emptyList();

  @Override
  public List<KeyPredicate> getKeyPredicates() {
    return keyPredicates;
  }

  public void setKeyPredicates(List<KeyPredicate> keyPredicates) {
    this.keyPredicates = keyPredicates;
  }

  @Override
  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  public void setNavigationProperty(EdmNavigationProperty edmNavigationProperty) {
    this.navigationProperty = edmNavigationProperty;
  }

  @Override
  public EdmEntitySet getEntitySet() {
    return targetEntitySet;
  }

  public void setEntitySet(EdmEntitySet edmEntitySet) {
    this.targetEntitySet = edmEntitySet;
  }

  @Override
  public String toString() {
    return "Navigation Property: " + navigationProperty + ", "
        + "Target Entity Set: " + targetEntitySet + ", "
        + "Key Predicates: " + keyPredicates;
  }

}
