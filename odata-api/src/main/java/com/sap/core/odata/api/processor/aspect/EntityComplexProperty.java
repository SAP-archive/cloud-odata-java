package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetComplexPropertyView;

/**
 * Execute a OData complex property request. 
 * 
 * @author SAP AG
 *
 */
public interface EntityComplexProperty {
  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityComplexProperty(GetComplexPropertyView uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityComplexProperty(String contentType) throws ODataException;
}
