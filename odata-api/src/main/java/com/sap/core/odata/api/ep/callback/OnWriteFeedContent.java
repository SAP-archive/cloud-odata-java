package com.sap.core.odata.api.ep.callback;

public interface OnWriteFeedContent {

  WriteFeedCallbackResult retrieveFeedResult(WriteFeedCallbackContext context);
  
}
