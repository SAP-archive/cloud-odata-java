package com.sap.core.odata.api.ep.callback;


public interface Callback {

  <T extends CallbackResult> T retriveResult(CallbackContext context, Class<T> callbackResultClass);

}
