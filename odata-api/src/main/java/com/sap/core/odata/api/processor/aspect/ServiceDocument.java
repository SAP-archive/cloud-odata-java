package com.sap.core.odata.api.processor.aspect;


import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;



public interface ServiceDocument {

  ODataResponse readServiceDocument(GetServiceDocumentView uriParserResultView) throws ODataException;

}
