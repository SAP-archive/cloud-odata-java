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

import java.util.List;

import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.FeedMetadata;
import com.sap.core.odata.api.ep.feed.ODataFeed;

public class ODataFeedImpl implements ODataFeed {

  private final List<ODataEntry> entries;
  private final FeedMetadata feedMetadata;

  public ODataFeedImpl(final List<ODataEntry> entries, final FeedMetadata feedMetadata) {
    this.entries = entries;
    this.feedMetadata = feedMetadata;

  }

  @Override
  public List<ODataEntry> getEntries() {
    return entries;
  }

  @Override
  public FeedMetadata getFeedMetadata() {
    return feedMetadata;
  }

}
