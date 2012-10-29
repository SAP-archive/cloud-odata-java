package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;


public interface Entity {
  ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataError;

  ODataResponse existsEntity(GetEntityCountView uriParserResultView) throws ODataError;

  ODataResponse updateEntity() throws ODataError;

  ODataResponse deleteEntity() throws ODataError;
  
}
