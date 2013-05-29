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
package com.sap.core.odata.api.servicedocument;

import java.util.List;

/**
 * A Collection
 * <p>Collection element describes a Collection
 * @author SAP AG
 */
public interface Collection {
  /**
   * Get the human-readable title for the Collection
   * 
   * @return {@link Title}
   */
  public Title getTitle();

  /**
   * Get the "href" attribute, whose value gives IRI of the Collection
   * 
   * @return href as String
   */
  public String getHref();

  /**
   * Get common attributes
   * 
   * @return {@link CommonAttributes}
   */
  public CommonAttributes getCommonAttributes();

  /**
   * Get the media range that specifies a type of representation 
   * that can be POSTed to a Collection
   * @return a list of {@link Accept}
   */
  public List<Accept> getAcceptElements();

  /**
   *
   * 
   * @return a list of {@link Categories}
   */
  public List<Categories> getCategories();

  /**
   * Get the list of extension elements
   * 
   * @return a list of {@link ExtensionElement}
   */
  public List<ExtensionElement> getExtesionElements();

}
