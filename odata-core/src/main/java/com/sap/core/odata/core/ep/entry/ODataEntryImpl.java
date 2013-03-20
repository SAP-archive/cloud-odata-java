package com.sap.core.odata.core.ep.entry;

import java.util.Map;

import com.sap.core.odata.api.ep.entry.EntryMetadata;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;

public class ODataEntryImpl implements ODataEntry {

  private Map<String, Object> data;
  private EntryMetadata entryMetadata;
  private MediaMetadata mediaMetadata;

  public ODataEntryImpl(final Map<String, Object> data, final MediaMetadata mediaMetadata, final EntryMetadata entryMetadata) {
    this.data = data;
    this.entryMetadata = entryMetadata;
    this.mediaMetadata = mediaMetadata;
  }

  @Override
  public Map<String, Object> getProperties() {
    return data;
  }

  @Override
  public MediaMetadata getMediaMetadata() {
    return mediaMetadata;
  }

  @Override
  public EntryMetadata getMetadata() {
    return entryMetadata;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ODataEntryImpl [data=" + data + ", entryMetadata=" + entryMetadata + ", mediaMetadata=" + mediaMetadata + "]";
  }
}
