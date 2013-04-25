package com.sap.core.odata.core.ep.entry;

public interface FeedMetadata {

  public int getInlineCount();

  //TODO: CA check if this has to be uri
  public String getNextLink();

}
