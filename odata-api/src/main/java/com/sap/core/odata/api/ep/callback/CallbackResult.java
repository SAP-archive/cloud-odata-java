package com.sap.core.odata.api.ep.callback;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public abstract class CallbackResult {

  Map<String, Callback> callbacks;

  URI baseUri;

  public Map<String, Callback> getCallbacks() {
    return callbacks;
  }

  public void setCallbacks(final HashMap<String, Callback> callbacks) {
    this.callbacks = callbacks;
  }

  public URI getBaseUri() {
    return baseUri;
  }

  public void setBaseUri(final URI baseUri) {
    this.baseUri = baseUri;
  }

}
