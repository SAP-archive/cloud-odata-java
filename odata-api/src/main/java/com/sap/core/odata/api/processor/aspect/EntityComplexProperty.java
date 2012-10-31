package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetComplexPropertyView;


public interface EntityComplexProperty {
  ODataResponse readEntityComplexProperty(GetComplexPropertyView uriParserResultView) throws ODataException;

  ODataResponse updateEntityComplexProperty() throws ODataException;
}
