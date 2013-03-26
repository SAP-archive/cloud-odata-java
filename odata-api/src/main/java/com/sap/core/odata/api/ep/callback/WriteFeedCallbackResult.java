package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;

/**
 * @author SAP AG
 */
/**
 * @author SAP AG
 *
 */
public class WriteFeedCallbackResult {

  Map<String, ODataCallback> callbacks;
  URI baseUri;
  List<Map<String, Object>> feedData;

  /**
   * @return
   */
  public Map<String, ODataCallback> getCallbacks() {
    return callbacks;
  }

  /**
   * @param callbacks
   */
  public void setCallbacks(final Map<String, ODataCallback> callbacks) {
    this.callbacks = callbacks;
  }

  /**
   * @return
   */
  public URI getBaseUri() {
    return baseUri;
  }

  /**
   * @param baseUri
   */
  public void setBaseUri(final URI baseUri) {
    this.baseUri = baseUri;
  }

  /**
   * @return
   */
  public List<Map<String, Object>> getFeedData() {
    return feedData;
  }

  /**
   * @param feedData
   */
  public void setFeedData(final List<Map<String, Object>> feedData) {
    this.feedData = feedData;
  }

}
