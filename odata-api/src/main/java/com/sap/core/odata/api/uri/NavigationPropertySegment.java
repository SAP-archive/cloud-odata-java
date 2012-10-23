package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;


public interface NavigationPropertySegment {

  public EdmNavigationProperty getNavigationProperty();

  public EdmEntitySet getTargetEntitySet();
  
}
