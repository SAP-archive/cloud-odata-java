package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;

/**
 * Execute a OData service document request. 
 * 
 * @author SAP AG
 */
public interface ServiceDocumentProcessor extends ODataProcessor {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readServiceDocument(GetServiceDocumentUriInfo uriInfo, String contentType) throws ODataException;

}
