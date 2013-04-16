package com.sap.core.odata.api.uri;

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;

/**
 * Navigation segment, consisting of a navigation property, its target entity set,
 * and, optionally, a list of key predicates to determine a single entity out of
 * the target entity set.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface NavigationSegment {

  /**
   * Gets the navigation property.
   * @return {@link EdmNavigationProperty} navigation property of this navigation segment
   */
  public EdmNavigationProperty getNavigationProperty();

  /**
   * Gets the target entity set.
   * @return {@link EdmEntitySet} entity set of this navigation segment
   */
  public EdmEntitySet getEntitySet();

  /**
   * Gets the key predicate for the target entity set.
   * @return List of {@link KeyPredicate}: key predicate of this navigation segment
   */
  public List<KeyPredicate> getKeyPredicates();

}
