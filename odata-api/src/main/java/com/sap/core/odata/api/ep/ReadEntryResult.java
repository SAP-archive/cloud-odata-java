package com.sap.core.odata.api.ep;

import java.util.Map;

public interface ReadEntryResult {

  public Map<String, Object> getProperties();

  public MediaMetadata getMediaMetadata();

  public EntryMetadata getMetadata();
}
