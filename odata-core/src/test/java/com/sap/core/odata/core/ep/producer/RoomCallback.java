package com.sap.core.odata.core.ep.producer;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.ep.callback.Callback;
import com.sap.core.odata.api.ep.callback.EntryCallbackResult;

public class RoomCallback implements Callback {

  private Map<String, Object> roomData;
  private HashMap<String, Callback> callbacksRoom;
  private URI baseUri;

  public RoomCallback(final Map<String, Object> data, final URI baseUri) {
    super();
    roomData = data;
    this.baseUri = baseUri;
  }

  @Override
  public EntryCallbackResult retriveResult(final Map<String, Object> key) {
    EntryCallbackResult result = new EntryCallbackResult();
    result.setData(roomData);
    result.setCallbacks(callbacksRoom);
    result.setBaseUri(baseUri);

    return result;
  }
}
