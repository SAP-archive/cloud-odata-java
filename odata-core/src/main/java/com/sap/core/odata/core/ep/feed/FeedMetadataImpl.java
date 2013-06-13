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
