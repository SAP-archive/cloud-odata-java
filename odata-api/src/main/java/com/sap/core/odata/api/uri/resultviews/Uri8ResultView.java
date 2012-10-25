package com.sap.core.odata.api.uri.resultviews;

import java.util.Map;

import com.sap.core.odata.api.enums.UriType;

public interface Uri8ResultView {
  /**
   * @return {@link UriType} the uri type
   */
  public UriType getUriType();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
