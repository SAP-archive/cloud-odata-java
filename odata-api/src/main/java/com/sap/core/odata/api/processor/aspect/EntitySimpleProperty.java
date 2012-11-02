package com.sap.core.odata.api.processor.aspect;


import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;


public interface EntitySimpleProperty {
  ODataResponse readEntitySimpleProperty(GetSimplePropertyView uriParserResultView) throws ODataException;

  ODataResponse updateEntitySimpleProperty() throws ODataException;
}
