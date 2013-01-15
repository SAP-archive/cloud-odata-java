package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetSimplePropertyUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * Execute a OData entity simple property value request. 
 * 
 * @author SAP AG
 */
public interface EntitySimplePropertyValue extends ProcessorFeature {

  /**
   * Reads the unformatted value of a simple property of an entity.
   * @param contentType the content type of the response
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Updates a simple property of an entity with an unformatted value.
   * @param uriInfo information about the request URI
   * @param content the content of the request, containing the new value
   * @param requestContentType the content type of the request body
   *                           (important for a binary property)
   * @param contentType the content type of the response
   * @param request 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimplePropertyValue(PutMergePatchUriInfo uriInfo, Object content, String requestContentType, String contentType) throws ODataException;

  /**
   * Deletes the value of a simple property of an entity.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntitySimplePropertyValue(DeleteUriInfo uriInfo, String contentType) throws ODataException;
}
