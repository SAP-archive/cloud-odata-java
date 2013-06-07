package com.sap.core.odata.api.ep.callback;

import java.util.List;
import java.util.Map;

public class TombstoneCallbackResult {

  private List<Map<String, Object>> deletedEntriesData;
  private String deltaLink;

  public List<Map<String, Object>> getDeletedEntriesData() {
    return deletedEntriesData;
  }

  public void setDeletedEntriesData(List<Map<String, Object>> deletedEntriesData) {
    this.deletedEntriesData = deletedEntriesData;
  }

  public String getDeltaLink() {
    return deltaLink;
  }

  public void setDeltaLink(String deltaLink) {
    this.deltaLink = deltaLink;
  }

}
