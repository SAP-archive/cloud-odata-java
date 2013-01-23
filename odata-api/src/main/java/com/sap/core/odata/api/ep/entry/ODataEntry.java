package com.sap.core.odata.api.ep.entry;

import java.util.Map;

/**
 * @author SAP AG
 */
public interface ODataEntry {

  public Map<String, Object> getProperties();

  public MediaMetadata getMediaMetadata();

  public EntryMetadata getMetadata();
}
