package com.sap.core.odata.api.ep.callback;

import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.ep.EntityProviderReadProperties;

public class ReadEntryCallbackContext {

  private final Map<String, Object> infos = new HashMap<String, Object>();
  private EntityProviderReadProperties readProperties;
  private final String title;
  private final String type;

  public ReadEntryCallbackContext(final EntityProviderReadProperties properties, final String type, final String title) {
    readProperties = properties;
    this.type = type;
    this.title = title;
  }

  public EntityProviderReadProperties getReadProperties() {
    return readProperties;
  }

  public void addInfos(final Map<String, String> infos) {
    if (infos != null) {
      this.infos.putAll(infos);
    }
  }

  public void addInfo(final String key, final Object value) {
    infos.put(key, value);
  }

  public Object getInfo(final String key) {
    return infos.get(key);
  }

  public String getTitle() {
    return title;
  }

  public String getType() {
    return type;
  }
}
