package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;

/**
 * Execute a OData service document request. 
 * 
 * @author SAP AG
 *
 */
public interface ServiceDocument {
  
  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readServiceDocument(GetServiceDocumentUriInfo uriParserResultView, String contentType) throws ODataException;

}
