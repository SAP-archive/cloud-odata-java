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

/**
 * {@link FeedMetadata} objects contain metadata information about one feed.
 * @author SAP AG
 *
 */
public interface FeedMetadata {

  /**
   * @return inlineCount may be null if no inlineCount is set.
   */
  public Integer getInlineCount();

  /**
   * @return nextLink may be null if no next link is set
   */
  public String getNextLink();

  /**
   * @return deltaLink may be null if no delta link is set
   */
  public String getDeltaLink();

}
