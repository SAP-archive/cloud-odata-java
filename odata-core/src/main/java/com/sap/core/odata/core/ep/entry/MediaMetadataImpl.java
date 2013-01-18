package com.sap.core.odata.core.ep.entry;

import com.sap.core.odata.api.ep.entry.MediaMetadata;

public class MediaMetadataImpl implements MediaMetadata {
  
  private String sourceLink;
  private String etag;
  private String contentType;
  private String editLink;
  
  @Override
  public String getSourceLink() {
    return sourceLink;
  }

  @Override
  public String getEtag() {
    return etag;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public String getEditLink() {
    return editLink;
  }

  public void setSourceLink(String sourceLink) {
    this.sourceLink = sourceLink;
  }
  
  public void setEtag(String etag) {
    this.etag = etag;
  }
  
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  
  public void setEditLink(String editLink) {
    this.editLink = editLink;
  }
}
