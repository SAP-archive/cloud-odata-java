package com.sap.core.odata.api.ep.callback;

import java.util.Map;

public interface Callback {

  CallbackResult retriveResult(Map<String, Object> key);

}
