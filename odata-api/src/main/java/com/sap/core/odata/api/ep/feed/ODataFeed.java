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
