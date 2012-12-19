package com.sap.core.odata.api.processor.aspect;

import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetCountView;
import com.sap.core.odata.api.uri.resultviews.GetEntitySetView;

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
  ODataResponse readEntitySet(GetEntitySetView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse countEntitySet(GetEntitySetCountView uriParserResultView, ContentType contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse createEntity(ContentType contentType) throws ODataException;
}
