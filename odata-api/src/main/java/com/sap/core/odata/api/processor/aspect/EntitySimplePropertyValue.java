package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;


public interface EntitySimplePropertyValue {
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView) throws ODataException;

  ODataResponse updateEntitySimplePropertyValue() throws ODataException;

  ODataResponse deleteEntitySimplePropertyValue() throws ODataException;
}
