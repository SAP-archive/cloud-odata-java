package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.Map;


public class CallbackResult {

   Map<String, Object> data;
   
   Map<String, Callback> callbacks;
   
   URI baseUri;

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  public Map<String, Callback> getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(Map<String, Callback> callbacks) {
    this.callbacks = callbacks;
  }

  public URI getBaseUri() {
    return baseUri;
  }

  public void setBaseUri(URI baseUri) {
    this.baseUri = baseUri;
  }

 
}
