package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;

public class WriteEntryCallbackResult {

  Map<String, ODataCallback> callbacks;

  URI baseUri;
  
  Map<String, Object> oneEntryData;
  
  public Map<String, ODataCallback> getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(final HashMap<String, ODataCallback> callbacks) {
    this.callbacks = callbacks;
  }

  public URI getBaseUri() {
    return baseUri;
  }

  public void setBaseUri(final URI baseUri) {
    this.baseUri = baseUri;
  }
  

  public Map<String, Object> getEntryData() {
    return oneEntryData;
  }

  public void setEntryData(final Map<String, Object> data) {
    oneEntryData = data;
  }

}
