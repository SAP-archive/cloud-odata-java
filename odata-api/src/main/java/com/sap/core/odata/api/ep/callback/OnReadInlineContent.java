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
package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.exception.ODataApplicationException;

/**
 * <p> 
 * Callback interface for the deep insert read calls (read of <code><m:inline></code> content). 
 * Typically the {@link #receiveReadProperties(EntityProviderReadProperties, EdmNavigationProperty)} method is called
 * when an inline navigation property is found and will be read.
 * </p> 
 * <p> 
 * The {@link #handleReadEntry(ReadEntryResult)} and {@link #handleReadFeed(ReadFeedResult)} methods are called 
 * after the inline navigation property was read and deliver the read (de-serialized) entity or list of entities.
 * If inlined navigation property is <code>nullable</code> and not set a {@link ReadEntryResult} is given with the 
 * <code>navigationPropertyName</code> and a <code>NULL</code> entry set.
 * </p>
 * 
 * @author SAP AG
 */
public interface OnReadInlineContent {

  /**
   * Receive (request) to be used {@link EntityProviderReadProperties} to read the found inline navigation property 
   * (<code>><m:inline>...</m:inline></code>).
   * 
   * @param readProperties read properties which are used to read enclosing parent entity
   * @param navigationProperty emd navigation property information of found inline navigation property
   * @return read properties which are used to read (de-serialize) found inline navigation property
   * @throws ODataApplicationException
   */
  EntityProviderReadProperties receiveReadProperties(EntityProviderReadProperties readProperties, EdmNavigationProperty navigationProperty) throws ODataApplicationException;

  /**
   * Handles reading (de-serialization) entry result.
   * The given {@link ReadEntryResult} object contains all contextual information
   * about the de-serialized inline navigation property and the entry as
   * {@link com.sap.core.odata.api.ep.entry.ODataEntry ODataEntry}.
   *
   * @param readEntryResult with contextual information about and de-serialized
   *                        inlined navigation property as
   *                        {@link com.sap.core.odata.api.ep.entry.ODataEntry ODataEntry}
   * @throws ODataApplicationException
   */
  void handleReadEntry(ReadEntryResult readEntryResult) throws ODataApplicationException;

  /**
   * Handles reading (de-serialization) entry result.
   * The given {@link ReadFeedResult} object contains all contextual information
   * about the de-serialized inline navigation property and the entry as
   * a list of {@link com.sap.core.odata.api.ep.entry.ODataEntry ODataEntry}.
   *
   * @param readFeedResult with contextual information about and de-serialized
   *                       inlined navigation property as a list of
   *                       {@link com.sap.core.odata.api.ep.entry.ODataEntry ODataEntry}
   * @throws ODataApplicationException
   */
  void handleReadFeed(ReadFeedResult readFeedResult) throws ODataApplicationException;
}
