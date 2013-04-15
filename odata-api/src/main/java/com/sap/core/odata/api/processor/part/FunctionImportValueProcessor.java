/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.processor.part;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;

/**
 * Execute an OData function import value request.
 * @author SAP AG
 */
public interface FunctionImportValueProcessor extends ODataProcessor {
  /**
   * Returns the unformatted value of a function import.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeFunctionImportValue(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException;
}
