package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmProperty;

/**
 * Expression tree node with information about selected properties and to be expanded links.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface ExpandSelectTreeNode {

  /**
   * Determines whether all properties (including navigation properties) have been selected.
   */
  public boolean isAll();

  /**
   * <p>Gets the list of explicitly selected {@link EdmProperty properties}.</p>
   * <p>This list does not contain any navigation properties.
   * It is empty if {@link #isAll()} returns <code>true</code>.</p>
   * @return List of selected properties
   */
  public List<EdmProperty> getProperties();

  /**
   * Gets the links that have to be included or expanded.
   * @return a Map from EdmNavigationProperty Name to its related {@link ExpandSelectTreeNode};
   *         if that node is <code>null</code>, a deferred link has been requested,
   *         otherwise the link must be expanded with information found in that node
   */
  public Map<String, ExpandSelectTreeNode> getLinks();
}
