package com.sap.core.odata.core.ep.entry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.ep.entry.EntryMetadata;

public class EntryMetadataImpl implements EntryMetadata {
  private String id;
  private String etag;
  private String uri;
  private Map<String, String> associationUris = new HashMap<String, String>();
  
  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getEtag() {
    return etag;
  }

  @Override
  public Map<String, String> getAssociationUris() {
    return Collections.unmodifiableMap(associationUris);
  }

  @Override
  public String getUri() {
    return uri;
  }

  public void setUri(String uri) {
    this.uri = uri;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setEtag(String etag) {
    this.etag = etag;
  }

  public void putAssociationUri(String navigationPropertyName, String uri) {
    associationUris.put(navigationPropertyName, uri);
  }
}
