package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.core.ep.entry.FeedMetadata;

public class FeedMetadataImpl implements FeedMetadata {

  private int inlineCount = 0;
  private String nextLink = "";
  
  public void setInlineCount(int inlineCount){
    this.inlineCount = inlineCount;
  }
  
  @Override
  public int getInlineCount() {
    return inlineCount;
  }

  public void setNextLink(String nextLink) {
    this.nextLink = nextLink;
  }
  
  @Override
  public String getNextLink() {
    return nextLink;
  }

}
