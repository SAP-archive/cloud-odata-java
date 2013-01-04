package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityLinkUriInfo;

/**
 * Execute an OData entity link request. 
 * 
 * @author SAP AG
 */
public interface EntityLink {
  /**
   * @param uriParserResultView
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityLink(GetEntityLinkUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param uriParserResultView
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntityLink(GetEntityLinkCountUriInfo uriParserResultView, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityLink(String contentType) throws ODataException;

  /**
   * @param uriParserResultView
   * @param contentType
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityLink(DeleteUriInfo uriParserResultView, String contentType) throws ODataException;
}
