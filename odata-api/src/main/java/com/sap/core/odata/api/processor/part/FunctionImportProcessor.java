/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.processor.part;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;

/**
 * Execute an OData function import request. 
 * @author SAP AG
 */
public interface FunctionImportProcessor extends ODataProcessor {
  /**
   * Executes a function import and returns the result.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeFunctionImport(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException;
}
