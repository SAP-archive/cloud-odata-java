package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetFunctionImportView;

/**
 * Execute a OData function import request. 
 * 
 * @author SAP AG
 *
 */
public interface FunctionImport {
  
  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeFunctionImport(GetFunctionImportView uriParserResultView) throws ODataException;

}
