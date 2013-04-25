package com.sap.core.odata.core.ep.entry;

import java.util.List;

import com.sap.core.odata.api.ep.entry.ODataEntry;

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
