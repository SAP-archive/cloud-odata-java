package com.sap.core.odata.core.ep.entry;

import java.util.List;

import com.sap.core.odata.api.ep.entry.ODataEntry;

public interface ODataFeed {

  public List<ODataEntry> getEntries();
  public FeedMetadata getFeedMetadata();
  
  
}
