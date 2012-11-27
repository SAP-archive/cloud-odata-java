package com.sap.core.odata.api.uri.resultviews;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.uri.EdmLiteral;

public interface GetFunctionImportView {
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
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
