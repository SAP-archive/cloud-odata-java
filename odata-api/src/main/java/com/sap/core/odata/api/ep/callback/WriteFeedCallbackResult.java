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

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ep.EntityProviderWriteProperties;

/**
 * Result of a callback. It contains the data of the feed which is to be expanded as well as the BaseUri of the feed. Further callbacks for this feed can also be set.
 * @author SAP AG
 *
 */
public class WriteFeedCallbackResult {

  EntityProviderWriteProperties inlineProperties;
  List<Map<String, Object>> feedData;

  /**
   * @return the inline provider properties
   */
  public EntityProviderWriteProperties getInlineProperties() {
    return inlineProperties;
  }

  /**
   * Sets the properties for the inline data. MUST NOT BE NULL.
   * @param inlineProperties
   */
  public void setInlineProperties(final EntityProviderWriteProperties inlineProperties) {
    this.inlineProperties = inlineProperties;
  }

  /**
   * @return the feed data as a list of maps
   */
  public List<Map<String, Object>> getFeedData() {
    return feedData;
  }

  /**
   * Sets the feed data as a list of maps.
   * @param feedData
   */
  public void setFeedData(final List<Map<String, Object>> feedData) {
    this.feedData = feedData;
  }

}
