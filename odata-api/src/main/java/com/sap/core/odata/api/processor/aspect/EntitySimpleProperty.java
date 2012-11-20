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
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimpleProperty(GetSimplePropertyView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimpleProperty() throws ODataException;
}
