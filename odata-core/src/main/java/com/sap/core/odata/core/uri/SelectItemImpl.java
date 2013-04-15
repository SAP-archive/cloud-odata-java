/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;

/**
 * @author SAP AG
 */
public class SelectItemImpl implements SelectItem {

  private List<NavigationPropertySegment> navigationPropertySegments = Collections.emptyList();
  private EdmProperty property;
  private boolean star;

  @Override
  public boolean isStar() {
    return star;
  }

  public void setStar(final boolean star) {
    this.star = star;
  }

  @Override
  public EdmProperty getProperty() {
    return property;
  }

  public void setProperty(final EdmProperty property) {
    this.property = property;
  }

  public void addNavigationPropertySegment(final NavigationPropertySegment segment) {
    if (navigationPropertySegments.equals(Collections.EMPTY_LIST)) {
      navigationPropertySegments = new ArrayList<NavigationPropertySegment>();
    }

    navigationPropertySegments.add(segment);
  }

  @Override
  public List<NavigationPropertySegment> getNavigationPropertySegments() {
    return navigationPropertySegments;
  }

}
