package com.sap.core.odata.core.ep;

import java.io.InputStream;

import com.sap.core.odata.api.ep.ODataEntityContent;

public class ODataEntityContentImpl implements ODataEntityContent {

  private InputStream contentStream;
  private String contentHeader;
  private String eTag;

  @Override
  public String getETag() {
    return eTag;
  }

  @Override
  public String getContentHeader() {
    return contentHeader;
  }

  @Override
  public InputStream getContent() {
    return contentStream;
  }

  public void setContentStream(InputStream contentStream) {
    this.contentStream = contentStream;
  }

  public void setETag(String eTag) {
    this.eTag = eTag;
  }

  public void setContentHeader(String contentHeader) {
    this.contentHeader = contentHeader;
  }

}
