package com.sap.core.odata.api.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.edm.EdmTyped;

public class SelectItem {

  private List<NavigationPropertySegment> navigationPropertySegments = Collections.emptyList();
  private EdmTyped property;
  private boolean star;

  public boolean isStar() {
    return star;
  }

  public void setStar(boolean star) {
    this.star = star;
  }

  public EdmTyped getProperty() {
    return property;
  }

  public void setProperty(EdmTyped property) {
    this.property = property;
  }

  public void addNavigationPropertySegment(NavigationPropertySegment segment) {
    if (navigationPropertySegments.equals(Collections.EMPTY_LIST))
      navigationPropertySegments = new ArrayList<NavigationPropertySegment>();

    navigationPropertySegments.add(segment);
  }

  public List<NavigationPropertySegment> getNavigationPropertySegments() {
    return navigationPropertySegments;
  }

}
