package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;

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
  ODataResponse readEntity(GetEntityUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntity(GetEntityCountUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntity(String contentType) throws ODataException;

  /**
   * @param uriParserResultView  a {@link DeleteUriInfo} object with information from the URI parser
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntity(DeleteUriInfo uriParserResultView, String contentType) throws ODataException;

}
