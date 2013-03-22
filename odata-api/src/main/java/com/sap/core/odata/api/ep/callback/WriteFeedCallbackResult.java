package com.sap.core.odata.api.ep.callback;

import java.util.List;
import java.util.Map;

public class WriteFeedCallbackResult extends CallbackResult {

  List<Map<String, Object>> feedData;

  public List<Map<String, Object>> getFeedData() {
    return feedData;
  }

  public void setFeedData(final List<Map<String, Object>> feedData) {
    this.feedData = feedData;
  }

}
