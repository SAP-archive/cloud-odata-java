package com.sap.core.odata.api.uri;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

/**
 * Navigation property segments
 * @author SAP AG
 */
public interface NavigationPropertySegment {

  /**
   * @return {@link EdmNavigationProperty} navigation property
   */
  public EdmNavigationProperty getNavigationProperty();

  /**
   * @return {@link EdmEntitySet} the target entity set
   */
  public EdmEntitySet getTargetEntitySet();

}
