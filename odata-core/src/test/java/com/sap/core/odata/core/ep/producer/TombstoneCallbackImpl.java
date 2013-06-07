package com.sap.core.odata.core.ep.producer;

import java.util.ArrayList;
import java.util.Map;

import com.sap.core.odata.api.ep.callback.TombstoneCallback;
import com.sap.core.odata.api.ep.callback.TombstoneCallbackResult;

public class TombstoneCallbackImpl implements TombstoneCallback {

  private ArrayList<Map<String, Object>> deletedEntriesData;
  private String deltaLink = null;

  public TombstoneCallbackImpl(final ArrayList<Map<String, Object>> deletedEntriesData, String deltaLink) {
    this.deletedEntriesData = deletedEntriesData;
    this.deltaLink = deltaLink;
  }

  @Override
  public TombstoneCallbackResult getTombstoneCallbackResult() {
    TombstoneCallbackResult result = new TombstoneCallbackResult();
    result.setDeletedEntriesData(deletedEntriesData);
    result.setDeltaLink(deltaLink);
    return result;
  }

}
