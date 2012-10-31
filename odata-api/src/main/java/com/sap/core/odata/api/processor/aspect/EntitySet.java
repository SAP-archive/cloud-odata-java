package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;


public interface EntitySet {
  ODataResponse readEntitySet(GetEntitySetView uriParserResultView) throws ODataException;

  ODataResponse countEntitySet(GetEntitySetCountView uriParserResultView) throws ODataException;

  ODataResponse createEntity() throws ODataException;
}
