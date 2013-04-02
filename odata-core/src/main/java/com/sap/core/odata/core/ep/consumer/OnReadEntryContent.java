package com.sap.core.odata.core.ep.consumer;

public interface OnReadEntryContent {

  ReadCallbackResult retrieveReadResult(ReadEntryCallbackContext callbackInfo);
}
