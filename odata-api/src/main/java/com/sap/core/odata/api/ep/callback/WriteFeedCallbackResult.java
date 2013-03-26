package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;

/**
 * Result of a callback. It contains the data of the feed which is to be expanded as well as the BaseUri of the feed. Further callbacks for this feed can also be set.
 * @author SAP AG
 *
 */
public class WriteFeedCallbackResult {

  Map<String, ODataCallback> callbacks;
  URI baseUri;
  List<Map<String, Object>> feedData;

  /**
   * @return callbacks for this feed
   */
  public Map<String, ODataCallback> getCallbacks() {
    return callbacks;
  }

  /**
   * Sets the callbacks for this feed. The callbacks will be called per entry and per navigation property that is to be expanded.
   * @param callbacks
   */
  public void setCallbacks(final Map<String, ODataCallback> callbacks) {
    this.callbacks = callbacks;
  }

  /**
   * @return the base URI of this feed
   */
  public URI getBaseUri() {
    return baseUri;
  }

  /**
   * Sets the base URI for this feed.
   * @param baseUri
   */
  public void setBaseUri(final URI baseUri) {
    this.baseUri = baseUri;
  }

  /**
   * @return the feed data as a list of maps
   */
  public List<Map<String, Object>> getFeedData() {
    return feedData;
  }

  /**
   * Sets the feed data as a list of maps.
   * @param feedData
   */
  public void setFeedData(final List<Map<String, Object>> feedData) {
    this.feedData = feedData;
  }

}
