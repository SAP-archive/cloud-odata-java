package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmTyped;

public interface SelectItem {

  public boolean isStar();

  public EdmTyped getProperty();

  public List<NavigationPropertySegment> getNavigationPropertySegments();

}
