package com.sap.core.odata.api.uri.resultviews;

import java.text.Format;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.uri.EdmLiteral;

public interface GetFunctionImportView {
  /**
   * @return {@link EdmFunctionImport} the funktion import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * @return {@link Format} the format
   */
  public ContentType getContentType();

  /**
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal<String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
