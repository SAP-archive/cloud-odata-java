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
package com.sap.core.odata.api.ep.feed;

import java.util.List;

import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * An {@link ODataFeed} object contains a list of {@link ODataEntry}s and the metadata associated with this feed.
 * @author SAP AG
 *
 */
public interface ODataFeed {

  /**
   * The returned list may be empty but never null.
   * @return list of {@link ODataEntry}s
   */
  public List<ODataEntry> getEntries();

  /**
   * @return {@link FeedMetadata} object
   */
  public FeedMetadata getFeedMetadata();

}
