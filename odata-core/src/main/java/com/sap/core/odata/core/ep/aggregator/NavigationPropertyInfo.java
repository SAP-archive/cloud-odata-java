/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.aggregator;

import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

public final class NavigationPropertyInfo {
  private String name;
  private EdmMultiplicity multiplicity;

  static NavigationPropertyInfo create(final EdmNavigationProperty property) throws EdmException {
    NavigationPropertyInfo info = new NavigationPropertyInfo();
    info.name = property.getName();
    info.multiplicity = property.getMultiplicity();
    return info;
  }

  @Override
  public String toString() {
    return name + "; multiplicity=" + multiplicity;
  }

  public EdmMultiplicity getMultiplicity() {
    return multiplicity;
  }

  public String getName() {
    return name;
  }
}
