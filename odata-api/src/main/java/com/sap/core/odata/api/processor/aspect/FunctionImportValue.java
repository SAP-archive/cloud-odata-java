package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetFunctionImportView;

/**
 * Execute a OData function import value request. 
 * 
 * @author SAP AG
 *
 */
public interface FunctionImportValue {
  
  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeFunctionImportValue(GetFunctionImportView uriParserResultView, ContentType contentType) throws ODataException;
}
