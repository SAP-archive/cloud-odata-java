package com.sap.core.odata.fit.ref;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;

/**
 * @author SAP AG
 */
public class AbstractRefJsonTest extends AbstractRefTest {
  @SuppressWarnings("unchecked")
  public StringMap<Object> getStringMap(final String body) {
    Gson gson = new Gson();
    StringMap<Object> map = gson.fromJson(body, StringMap.class);
    StringMap<Object> dMap = (StringMap<Object>) map.get("d");
    return dMap;
  }
}
