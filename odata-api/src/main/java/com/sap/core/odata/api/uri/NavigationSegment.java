package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

/**
 * Navigation segment interface
 * @author SAP AG
 */
public interface NavigationSegment {

  /**
   * @return list of {@link KeyPredicate} key predicate of this navigation segment
   */
  public List<KeyPredicate> getKeyPredicates();

  /**
   * @return {@link EdmNavigationProperty} navigation property of this navigation segment
   */
  public EdmNavigationProperty getNavigationProperty();

  /**
   * @return {@link EdmEntitySet} entity set of this navigation segment
   */
  public EdmEntitySet getEntitySet();

}
