package com.sap.core.odata.core.uri;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.NavigationPropertySegment;

/**
 * @author SAP AG
 */
public class NavigationPropertySegmentImpl implements NavigationPropertySegment {

  private EdmNavigationProperty navigationProperty;
  private EdmEntitySet targetEntitySet;

  @Override
  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  public void setNavigationProperty(final EdmNavigationProperty navigationProperty) {
    this.navigationProperty = navigationProperty;
  }

  @Override
  public EdmEntitySet getTargetEntitySet() {
    return targetEntitySet;
  }

  public void setTargetEntitySet(final EdmEntitySet targetEntitySet) {
    this.targetEntitySet = targetEntitySet;
  }

  @Override
  public String toString() {
    return "Navigation Property: " + navigationProperty + ", Target Entity Set: " + targetEntitySet;
  }
}
