package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetMediaResourceUriInfo;

/**
 * Execute a OData entity media request. 
 * 
 * @author SAP AG
 *
 */
public interface EntityMedia {
  
  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityMedia(GetMediaResourceUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityMedia(String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityMedia(String contentType) throws ODataException;
}
