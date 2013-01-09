package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetFunctionImportUriInfo;

/**
 * Execute a OData function import value request. 
 * 
 * @author SAP AG
 */
public interface FunctionImportValue extends ProcessorFeature {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeFunctionImportValue(GetFunctionImportUriInfo uriInfo, String contentType) throws ODataException;
}
