package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;

/**
 * Execute a OData entity request. 
 * 
 * @author SAP AG
 *
 */
public interface Entity {

  /**
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse readEntity(GetEntityView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntity(GetEntityCountView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntity() throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntity() throws ODataException;

}
