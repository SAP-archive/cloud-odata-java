/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.processor.part;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
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
