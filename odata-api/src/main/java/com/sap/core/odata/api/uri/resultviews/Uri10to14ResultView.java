package com.sap.core.odata.api.uri.resultviews;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.uri.UriLiteral;

public interface Uri10to14ResultView {
  /**
   * @return {@link EdmFunctionImport} the funktion import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * @return {@link Format} the format
   */
  public Format getFormat();

  /**
   * @return String the customer format
   */
  public String getCustomFormat();

  /**
   * @return Map of {@literal <String,} {@link UriLiteral}{@literal >} function import parameters
   */
  public Map<String, UriLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
