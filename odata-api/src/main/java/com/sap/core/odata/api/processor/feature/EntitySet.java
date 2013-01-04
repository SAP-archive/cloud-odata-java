package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;

/**
 * Execute a OData entity set request. 
 * 
 * @author SAP AG
 *
 */
public interface EntitySet {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySet(GetEntitySetUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse countEntitySet(GetEntitySetCountUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse createEntity(String contentType) throws ODataException;
}
