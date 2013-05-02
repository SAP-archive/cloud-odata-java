package com.sap.core.odata.fit.ref;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;

/**
 * @author SAP AG
 */
public class AbstractRefJsonTest extends AbstractRefTest {
  public StringMap<?> getStringMap(final String body) {
    Gson gson = new Gson();
    final StringMap<?> map = gson.fromJson(body, new TypeToken<StringMap<?>>() {}.getType());
    if (map.get("d") instanceof StringMap<?>)
      return (StringMap<?>) map.get("d");
    else
      return map;
  }
}