package com.sap.core.odata.api.processor.aspect;

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
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimplePropertyValue() throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntitySimplePropertyValue() throws ODataException;
}
