package com.sap.core.odata.api.uri.resultviews;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.uri.EdmLiteral;

/**
 * @author SAP AG
 */
public interface GetFunctionImportView {
  /**
   * @return {@link EdmFunctionImport} the function import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * @return {@link String} the content type
   */
  public String getContentType();

  /**
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
