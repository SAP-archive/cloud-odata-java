package com.sap.core.odata.core.ep.consumer;

import java.util.HashMap;
import java.util.Map;

public class CallbackInfo {

  private final Map<String, String> infos = new HashMap<String, String>();

  public void addInfos(Map<String, String> infos) {
    if(infos != null) {
      this.infos.putAll(infos);
    }
  }
  
  public void addInfo(String key, String value) {
    infos.put(key, value);
  }
  
  public String getInfo(String key) {
    return infos.get(key);
  }
  
  public void setUri(String uri) {
    infos.put("URI", uri);
  }

  public String getUri() {
    return infos.get("URI");
  }
  
  public String getTitle() {
    return infos.get("title");
  }
}
