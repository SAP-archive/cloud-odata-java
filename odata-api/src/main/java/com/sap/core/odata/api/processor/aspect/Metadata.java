package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;

/**
 * Execute a OData metadata request. 
 * 
 * @author SAP AG
 *
 */
public interface Metadata {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readMetadata(GetMetadataView uriParserResultView, String contentType) throws ODataException;

}
