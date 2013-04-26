package com.sap.core.odata.core.ep.feed;

import java.util.List;

import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.FeedMetadata;
import com.sap.core.odata.api.ep.feed.ODataFeed;

public class ODataFeedImpl implements ODataFeed {

  private final List<ODataEntry> entries;
  private final FeedMetadata feedMetadata;

  public ODataFeedImpl(List<ODataEntry> entries, FeedMetadata feedMetadata) {
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
