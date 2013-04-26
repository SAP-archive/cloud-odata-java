package com.sap.core.odata.api.ep.feed;

import java.util.List;

import com.sap.core.odata.api.ep.entry.ODataEntry;

public interface ODataFeed {

  public List<ODataEntry> getEntries();
  public FeedMetadata getFeedMetadata();
  
  
}
