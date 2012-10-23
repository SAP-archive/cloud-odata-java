package com.sap.core.odata.core.uri;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;


public class NavigationPropertySegment {

  private EdmNavigationProperty navigationProperty;
  private EdmEntitySet targetEntitySet;
  
  
  public EdmNavigationProperty getNavigationProperty() {
    return navigationProperty;
  }
  public void setNavigationProperty(EdmNavigationProperty navigationProperty) {
    this.navigationProperty = navigationProperty;
  }
  public EdmEntitySet getTargetEntitySet() {
    return targetEntitySet;
  }
  public void setTargetEntitySet(EdmEntitySet targetEntitySet) {
    this.targetEntitySet = targetEntitySet;
  }
  
  
}
