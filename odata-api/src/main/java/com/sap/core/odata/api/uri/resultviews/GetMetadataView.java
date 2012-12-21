package com.sap.core.odata.api.uri.resultviews;

import java.util.Map;

/**
 * @author SAP AG
 */
public interface GetMetadataView {
  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
