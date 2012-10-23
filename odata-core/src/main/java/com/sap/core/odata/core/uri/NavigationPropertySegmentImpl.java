package com.sap.core.odata.core.uri;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.NavigationPropertySegment;

public class NavigationPropertySegmentImpl implements NavigationPropertySegment {

  private EdmNavigationProperty navigationProperty;
  private EdmEntitySet targetEntitySet;

  @Override
  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }

  public void setNavigationProperty(EdmNavigationProperty navigationProperty) {
    this.navigationProperty = navigationProperty;
  }

  @Override
  public EdmEntitySet getTargetEntitySet() {
    return targetEntitySet;
  }

  public void setTargetEntitySet(EdmEntitySet targetEntitySet) {
    this.targetEntitySet = targetEntitySet;
  }

}
