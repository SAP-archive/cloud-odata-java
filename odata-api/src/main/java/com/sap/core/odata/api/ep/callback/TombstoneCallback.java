package com.sap.core.odata.api.ep.callback;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.ODataCallback;

//TODO: document
public interface TombstoneCallback extends ODataCallback {

  public static final String TOMBSTONE_CALLBACK_KEY = "~tombstoneCallback";

  List<Map<String, Object>> getTombstoneData();

}
