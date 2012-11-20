package com.sap.core.odata.api.processor.aspect;

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
  * @return a {@link ODataResponse} object
  * @throws ODataException
  */
  ODataResponse readEntityLink(GetEntityLinkView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntityLink(GetEntityLinkCountView uriParserResultView) throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityLink() throws ODataException;

  /**
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityLink() throws ODataException;
}
