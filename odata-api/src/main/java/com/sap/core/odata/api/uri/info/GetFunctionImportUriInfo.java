package com.sap.core.odata.api.uri.info;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;

/**
 * @author SAP AG
 */
public interface GetFunctionImportUriInfo {
  /**
   * @return {@link EdmFunctionImport} the function import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * @return the format (as set as <code>$format</code> query parameter) or null
   */
  public String getFormat();

  /**
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters or EmptyMap
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * @return Map of {@literal <String, String>} custom query options or EmptyMap
   */
  public Map<String, String> getCustomQueryOptions();
}
