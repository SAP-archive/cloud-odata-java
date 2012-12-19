package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntityLinkView;

/**
 * Execute a OData entity link request. 
 * 
 * @author SAP AG
 *
 */
public interface EntityLink {
  /**
  * @param contentType 
   * @return a {@link ODataResponse} object
  * @throws ODataException
  */
  ODataResponse readEntityLink(GetEntityLinkView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityLink(ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityLink(ContentType contentType) throws ODataException;
}
