/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.uri.info;

import java.util.Map;

/**
 * Access to the parts of the request URI that are relevant for GET requests
 * of the metadata document.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface GetMetadataUriInfo {
  /**
   * Gets the custom query options as Map from option names to their
   * corresponding String values, or an empty list if no custom query options
   * are given in the URI.
   * @return Map of {@literal <String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
