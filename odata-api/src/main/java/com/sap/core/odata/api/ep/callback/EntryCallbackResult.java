package com.sap.core.odata.api.ep.callback;

import java.util.Map;

public class EntryCallbackResult extends CallbackResult {

  Map<String, Object> oneEntryData;

  public Map<String, Object> getData() {
    return oneEntryData;
  }

  public void setData(final Map<String, Object> data) {
    oneEntryData = data;
  }

}
