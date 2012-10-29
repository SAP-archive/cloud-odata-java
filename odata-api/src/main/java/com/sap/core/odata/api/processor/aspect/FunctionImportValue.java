package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetFunctionImportView;

public interface FunctionImportValue {
  ODataResponse executeFunctionImportValue(GetFunctionImportView uriParserResultView) throws ODataError;
}
