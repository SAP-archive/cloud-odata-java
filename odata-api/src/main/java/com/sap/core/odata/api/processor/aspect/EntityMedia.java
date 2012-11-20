package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetMediaResourceView;

/**
 * Execute a OData entity media request. 
 * 
 * @author SAP AG
 *
 */
public interface EntityMedia {
  
  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityMedia(GetMediaResourceView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityMedia() throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityMedia() throws ODataException;
}
