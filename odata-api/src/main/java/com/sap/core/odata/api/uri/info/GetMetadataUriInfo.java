package com.sap.core.odata.api.uri.info;

import java.util.Map;

/**
 * @author SAP AG
 */
public interface GetMetadataUriInfo {
  /**
   * @return Map of {@literal <String, String>} custom query options or EmptyMap
   */
  public Map<String, String> getCustomQueryOptions();
}
