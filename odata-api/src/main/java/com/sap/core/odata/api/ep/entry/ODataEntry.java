package com.sap.core.odata.api.ep.entry;

import java.util.Map;

public interface ODataEntry {

  public Map<String, Object> getProperties();

  public MediaMetadata getMediaMetadata();

  public EntryMetadata getMetadata();
}
