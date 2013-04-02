package com.sap.core.odata.api.ep.callback;

public interface OnReadEntryContent {

  ReadCallbackResult retrieveReadResult(ReadEntryCallbackContext callbackInfo);
}
