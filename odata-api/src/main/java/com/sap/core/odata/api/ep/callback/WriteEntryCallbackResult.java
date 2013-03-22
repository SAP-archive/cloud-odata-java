package com.sap.core.odata.api.ep.callback;

import java.util.Map;

public class WriteEntryCallbackResult extends CallbackResult {

  Map<String, Object> oneEntryData;

  public Map<String, Object> getEntryData() {
    return oneEntryData;
  }

  public void setEntryData(final Map<String, Object> data) {
    oneEntryData = data;
  }

}
