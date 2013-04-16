package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

/**
 * Navigation property segment, consisting of a navigation property and its
 * target entity set.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface NavigationPropertySegment {

  /**
   * Gets the navigation property.
   * @return {@link EdmNavigationProperty} navigation property
   */
  public EdmNavigationProperty getNavigationProperty();

  /**
   * Gets the target entity set.
   * @return {@link EdmEntitySet} the target entity set
   */
  public EdmEntitySet getTargetEntitySet();

}
