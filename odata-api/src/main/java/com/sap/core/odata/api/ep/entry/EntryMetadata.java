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
package com.sap.core.odata.api.ep.entry;

import java.util.List;

/**
 * {@link EntryMetadata} contains all metadata for an {@link ODataEntry}.
 */
public interface EntryMetadata {

  /**
   * Gets the URI of this entry.
   * 
   * @return the URI
   */
  public abstract String getUri();

  /**
   * Gets the association URIs for a given navigation property.
   * 
   * @param navigationPropertyName the name of the navigation property
   * @return the list of URIs for the given navigation property
   */
  public abstract List<String> getAssociationUris(String navigationPropertyName);

  /**
   * Gets the entity tag for this entry.
   * 
   * @return the entity tag
   */
  public abstract String getEtag();

  /**
   * Gets the ID of this entry.
   * 
   * @return the ID
   */
  public abstract String getId();

}
