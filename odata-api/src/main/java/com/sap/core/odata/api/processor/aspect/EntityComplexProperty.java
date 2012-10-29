package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetComplexPropertyView;

public interface EntityComplexProperty {
  ODataResponse readEntityComplexProperty(GetComplexPropertyView uriParserResultView) throws ODataError;

  ODataResponse updateEntityComplexProperty() throws ODataError;
}
