/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.processor.part;

import java.io.InputStream;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntityUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * Execute a OData entity request. 
 * 
 * @author SAP AG
 */
public interface EntityProcessor extends ODataProcessor {

  /**
   * Reads an entity.
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Checks whether an entity exists.
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntity(GetEntityCountUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Updates an entity.
   * @param uriInfo information about the request URI
   * @param content the content of the request, containing the updated entity data
   * @param requestContentType the content type of the request body
   * @param merge if <code>true</code>, properties not present in the data are left unchanged;
   *              if <code>false</code>, they are reset
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntity(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, boolean merge, String contentType) throws ODataException;

  /**
   * Deletes an entity.
   * @param uriInfo  a {@link DeleteUriInfo} object with information from the URI parser
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntity(DeleteUriInfo uriInfo, String contentType) throws ODataException;

}
