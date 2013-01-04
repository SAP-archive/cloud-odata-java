package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.DeleteResultView;
import com.sap.core.odata.api.uri.resultviews.GetEntityCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityView;

/**
 * Execute a OData entity request. 
 * 
 * @author SAP AG
 */
public interface Entity {

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntity(GetEntityView uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntity(GetEntityCountView uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntity(String contentType) throws ODataException;

  /**
   * @param uriParserResultView  a {@link DeleteResultView} object with information from the URI parser
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntity(DeleteResultView uriParserResultView, String contentType) throws ODataException;

}
