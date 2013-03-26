package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;

/**
 * Result of a callback. It contains the data of the entry which is to be expanded as well as the BaseUri of the entry. Further callbacks for this entry can also be set.
 * @author SAP AG
 */
public class WriteEntryCallbackResult {

  //TODO: Check if itcan be null
  Map<String, ODataCallback> callbacks;
  URI baseUri;
  Map<String, Object> oneEntryData;

  /**
   * @return callbacks for this entry
   */
  public Map<String, ODataCallback> getCallbacks() {
    return callbacks;
  }

  /**
   * Sets the callbacks for this entry. Callbacks have to be marked by the {@link ODataCallback} interface.
   * @param callbacks
   */
  public void setCallbacks(final Map<String, ODataCallback> callbacks) {
    this.callbacks = callbacks;
  }


  /**
   * @param baseUri of the feed
   */
  public void setBaseUri(final URI baseUri) {
    this.baseUri = baseUri;
  }

  /**
   * @return the data for the entry as a map
   */
  public Map<String, Object> getEntryData() {
    return oneEntryData;
  }

  /**
   * @param data for the entry as a map
   */
  public void setEntryData(final Map<String, Object> data) {
    oneEntryData = data;
  }

  /**
   * @return the base uri for this entry
   */
  public URI getBaseUri() {
    return baseUri;
  }

}
