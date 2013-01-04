package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;

/**
 * Execute a OData entity simple property value request. 
 * 
 * @author SAP AG
 *
 */
public interface EntitySimplePropertyValue {
  
  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimplePropertyValue(String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntitySimplePropertyValue(String contentType) throws ODataException;
}
