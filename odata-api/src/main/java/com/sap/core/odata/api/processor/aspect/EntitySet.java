package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;

public interface EntitySet {
  ODataResponse readEntitySet(GetEntitySetView uriParserResultView) throws ODataError;

  ODataResponse countEntitySet(GetEntitySetCountView uriParserResultView) throws ODataError;

  ODataResponse createEntity() throws ODataError;
}
