package com.sap.core.odata.core.ep;

import java.util.Map;

import com.sap.core.odata.api.ep.EntryMetadata;
import com.sap.core.odata.api.ep.MediaMetadata;
import com.sap.core.odata.api.ep.ReadEntryResult;

public class ReadEntryResultImpl implements ReadEntryResult {

  private Map<String, Object> data;
  private EntryMetadata entryMetadata;
  private MediaMetadata mediaMetadata;
  
  public ReadEntryResultImpl(Map<String, Object> data, MediaMetadata mediaMetadata, EntryMetadata entryMetadata) {
    this.data = data;
    this.entryMetadata = entryMetadata;
    this.mediaMetadata = mediaMetadata;
  }

  @Override
  public Map<String, Object> getData() {
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
}
