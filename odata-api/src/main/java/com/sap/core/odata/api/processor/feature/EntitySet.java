package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;

/**
 * Execute a OData entity set request. 
 * 
 * @author SAP AG
 *
 */
public interface EntitySet extends ProcessorFeature {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse countEntitySet(GetEntitySetCountUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param request 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse createEntity(PostUriInfo uriInfo, ODataRequest request) throws ODataException;
}
