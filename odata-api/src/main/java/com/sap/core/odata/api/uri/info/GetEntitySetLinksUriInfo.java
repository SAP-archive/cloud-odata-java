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
package com.sap.core.odata.api.uri.info;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.NavigationSegment;
import com.sap.core.odata.api.uri.expression.FilterExpression;

/**
 * Access to the parts of the request URI that are relevant for GET requests
 * of the URIs of entities.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface GetEntitySetLinksUriInfo {
  /**
   * Gets the target entity container.
   * @return {@link EdmEntityContainer} the target entity container
   */
  public EdmEntityContainer getEntityContainer();

  /**
   * Gets the start entity set - identical to the target entity set if no navigation
   * has been used.
   * @return {@link EdmEntitySet}
   */
  public EdmEntitySet getStartEntitySet();

  /**
   * Gets the target entity set after navigation.
   * @return {@link EdmEntitySet} target entity set
   */
  public EdmEntitySet getTargetEntitySet();

  /**
   * Gets the function import.
   * @return {@link EdmFunctionImport} the function import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * Gets the target entity type of the request.
   * @return {@link EdmType} the target type
   */
  public EdmType getTargetType();

  /**
   * Gets the key predicates used to select a single entity out of the start entity set,
   * or an empty list if not used.
   * @return List of {@link KeyPredicate}
   * @see #getStartEntitySet()
   */
  public List<KeyPredicate> getKeyPredicates();

  /**
   * Gets the navigation segments, or an empty list if no navigation has been used.
   * @return List of {@link NavigationSegment}
   */
  public List<NavigationSegment> getNavigationSegments();

  /**
   * Determines whether $links has been used in the request URI.
   * @return whether $links has been used
   */
  public boolean isLinks();

  /**
   * Gets the value of the $format system query option.
   * @return the format (as set as <code>$format</code> query parameter) or null
   */
  public String getFormat();

  /**
   * Gets the value of the $filter system query option as root object of the
   * expression tree built during URI parsing.
   * @return the filter expression or null
   */
  public FilterExpression getFilter();

  /**
   * Gets the value of the $inlinecount system query option.
   * @return {@link InlineCount} the inline count or null
   */
  public InlineCount getInlineCount();

  /**
   * Gets the value of the $skiptoken system query option.
   * @return skip token or null
   */
  public String getSkipToken();

  /**
   * Gets the value of the $skip system query option.
   * @return skip or null
   */
  public Integer getSkip();

  /**
   * Gets the value of the $top system query option.
   * @return top or null
   */
  public Integer getTop();

  /**
   * Gets the parameters of a function import as Map from parameter names to
   * their corresponding typed values, or an empty list if no function import
   * is used or no parameters are given in the URI.
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * Gets the custom query options as Map from option names to their
   * corresponding String values, or an empty list if no custom query options
   * are given in the URI.
   * @return Map of {@literal <String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
