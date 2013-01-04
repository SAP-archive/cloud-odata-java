package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;

/**
 * Execute a OData entity simple property request. 
 * 
 * @author SAP AG
 *
 */
public interface EntitySimpleProperty {
  
  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimpleProperty(GetSimplePropertyView uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimpleProperty(String contentType) throws ODataException;
}
