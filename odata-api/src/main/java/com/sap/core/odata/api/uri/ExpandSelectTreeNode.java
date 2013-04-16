/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
