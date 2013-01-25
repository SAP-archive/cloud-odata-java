package com.sap.core.odata.api.processor;

import java.io.InputStream;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.info.GetEntitySetCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;

/**
 * Execute a OData entity set request. 
 * 
 * @author SAP AG
 *
 */
public interface EntitySetProcessor extends ODataProcessor {

  /**
   * Reads entities.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySet(GetEntitySetUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Counts the number of requested entities.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse countEntitySet(GetEntitySetCountUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Creates an entity.
   * @param uriInfo information about the request URI
   * @param content the content of the request, containing the data of the new entity
   * @param requestContentType the content type of the request body
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse createEntity(PostUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException;
}
