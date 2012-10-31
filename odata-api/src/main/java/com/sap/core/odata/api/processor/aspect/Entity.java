package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;


public interface Entity {
  ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataException;

  ODataResponse existsEntity(GetEntityCountView uriParserResultView) throws ODataException;

  ODataResponse updateEntity() throws ODataException;

  ODataResponse deleteEntity() throws ODataException;
  
}
