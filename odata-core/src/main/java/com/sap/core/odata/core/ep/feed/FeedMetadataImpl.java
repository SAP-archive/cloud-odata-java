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
package com.sap.core.odata.core.ep.feed;

import com.sap.core.odata.api.ep.feed.FeedMetadata;

public class FeedMetadataImpl implements FeedMetadata {

  private Integer inlineCount = null;
  private String nextLink = null;
  private String deltaLink;

  public void setInlineCount(final int inlineCount) {
    this.inlineCount = inlineCount;
  }

  @Override
  public Integer getInlineCount() {
    return inlineCount;
  }

  public void setNextLink(final String nextLink) {
    this.nextLink = nextLink;
  }

  @Override
  public String getNextLink() {
    return nextLink;
  }

  public void setDeltaLink(final String deltaLink) {
    this.deltaLink = deltaLink;
  }

  @Override
  public String getDeltaLink() {
    return deltaLink;
  }

}
