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
