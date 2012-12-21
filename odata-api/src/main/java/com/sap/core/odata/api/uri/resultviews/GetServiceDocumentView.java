package com.sap.core.odata.api.uri.resultviews;

import com.sap.core.odata.api.enums.ContentType;

/**
 * @author SAP AG
 */
public interface GetServiceDocumentView {
  /**
   * @return {@link ContentType} the content type
   */
  public ContentType getContentType();
}
