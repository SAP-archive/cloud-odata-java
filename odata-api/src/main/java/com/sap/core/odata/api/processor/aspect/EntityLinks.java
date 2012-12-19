package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetLinksView;

/**
 * Execute a OData entity links request. 
 * 
 * @author SAP AG
 *
 */
public interface EntityLinks {
  /**
   * @param contentType 
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse readEntityLinks(GetEntitySetLinksView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse countEntityLinks(GetEntitySetLinksCountView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse createEntityLink(ContentType contentType) throws ODataException;
}
