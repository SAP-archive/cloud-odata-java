package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;

/**
 * Result of a callback. It contains the data of the entry which is to be expanded as the BaseUri of the entry. Further callbacks for this entry can also be set.
 * @author SAP AG
 */
public class WriteEntryCallbackResult {

  //TODO: Check if itcan be null
  Map<String, ODataCallback> callbacks;
  URI baseUri;
  Map<String, Object> oneEntryData;

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
   * @param baseUri
   */
  public void setBaseUri(final URI baseUri) {
    this.baseUri = baseUri;
  }

  /**
   * @return
   */
  public Map<String, Object> getEntryData() {
    return oneEntryData;
  }

  /**
   * @param data
   */
  public void setEntryData(final Map<String, Object> data) {
    oneEntryData = data;
  }

  /**
   * @return
   */
  public URI getBaseUri() {
    return baseUri;
  }

}
