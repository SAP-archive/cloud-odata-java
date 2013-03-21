package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

public class CallbackInfo {

  private final Map<String, String> infos = new HashMap<String, String>();

  public void addInfos(final Map<String, String> infos) {
    if (infos != null) {
      this.infos.putAll(infos);
    }
  }

  public void addInfo(final String key, final String value) {
    infos.put(key, value);
  }

  public String getInfo(final String key) {
    return infos.get(key);
  }

  public void setUri(final String uri) {
    infos.put("URI", uri);
  }

  public String getUri() {
    return infos.get("URI");
  }

  public String getTitle() {
    return infos.get("title");
  }
}
