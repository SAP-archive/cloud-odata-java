/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.uri.info;

import java.util.Map;

import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmLiteral;

/**
 * Access to the parts of the request URI that are relevant for requests
 * of function imports.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface GetFunctionImportUriInfo {
  /**
   * Gets the function import.
   * @return {@link EdmFunctionImport} the function import
   */
  public EdmFunctionImport getFunctionImport();

  /**
   * Gets the value of the $format system query option.
   * @return the format (as set as <code>$format</code> query parameter) or null
   */
  public String getFormat();

  /**
   * Gets the parameters of a function import as Map from parameter names to
   * their corresponding typed values, or an empty list if no function import
   * is used or no parameters are given in the URI.
   * @return Map of {@literal <String,} {@link EdmLiteral}{@literal >} function import parameters
   */
  public Map<String, EdmLiteral> getFunctionImportParameters();

  /**
   * Gets the custom query options as Map from option names to their
   * corresponding String values, or an empty list if no custom query options
   * are given in the URI.
   * @return Map of {@literal <String, String>} custom query options
   */
  public Map<String, String> getCustomQueryOptions();
}
