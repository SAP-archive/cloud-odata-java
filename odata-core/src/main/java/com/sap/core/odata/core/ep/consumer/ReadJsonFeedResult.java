package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.ReadFeedResult;
import com.sap.core.odata.api.ep.feed.ODataFeed;

public class ReadJsonFeedResult extends ReadFeedResult {
 
  private final ODataFeed feed;

  public ODataFeed getFeed() {
    return feed;
  }

  public ReadJsonFeedResult(EntityProviderReadProperties properties, EdmNavigationProperty navigationProperty, ODataFeed feed) {
    super(properties, navigationProperty, null);
    this.feed = feed;
  }

}
