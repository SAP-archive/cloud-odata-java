package com.sap.core.odata.api.uri.resultviews;

import java.text.Format;

import com.sap.core.odata.api.enums.ContentType;

public interface GetServiceDocumentView {
  /**
   * @return {@link Format} the format
   */
  public ContentType getContentType();
}
