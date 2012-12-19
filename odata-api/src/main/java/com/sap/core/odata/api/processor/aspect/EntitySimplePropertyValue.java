package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetSimplePropertyView;

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
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimplePropertyValue(ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntitySimplePropertyValue(ContentType contentType) throws ODataException;
}
