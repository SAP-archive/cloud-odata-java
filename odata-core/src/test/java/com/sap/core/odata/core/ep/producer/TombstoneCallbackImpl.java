package com.sap.core.odata.core.ep.producer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ep.callback.TombstoneCallback;

public class TombstoneCallbackImpl implements TombstoneCallback {

  private ArrayList<Map<String, Object>> deletedRoomsData;

  public TombstoneCallbackImpl(final ArrayList<Map<String, Object>> deletedRoomsData) {
    this.deletedRoomsData = deletedRoomsData;
  }

  @Override
  public List<Map<String, Object>> getTombstoneData() {
    return deletedRoomsData;
  }

}
