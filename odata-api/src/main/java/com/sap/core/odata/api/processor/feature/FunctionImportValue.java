package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;

/**
 * Execute an OData function import value request.
 * @author SAP AG
 */
public interface FunctionImportValue extends ProcessorFeature {
  /**
   * Returns the unformatted value of a function import.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeFunctionImportValue(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException;
}
