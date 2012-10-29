package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;

public interface EntitySimplePropertyValue {
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView) throws ODataError;

  ODataResponse updateEntitySimplePropertyValue() throws ODataError;

  ODataResponse deleteEntitySimplePropertyValue() throws ODataError;
}
