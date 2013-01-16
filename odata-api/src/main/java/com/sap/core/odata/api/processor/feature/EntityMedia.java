package com.sap.core.odata.api.processor.feature;

import java.io.InputStream;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.DeleteUriInfo;
import com.sap.core.odata.api.uri.info.GetMediaResourceUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * Execute an OData entity media request
 * @author SAP AG
 */
public interface EntityMedia extends ProcessorFeature {

  /**
   * Reads the media resource of an entity.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityMedia(GetMediaResourceUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Updates the media resource of an entity.
   * @param uriInfo information about the request URI
   * @param content the content of the request
   * @param requestContentType the content type of the request body
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityMedia(PutMergePatchUriInfo uriInfo, InputStream content, String requestContentType, String contentType) throws ODataException;

  /**
   * Deletes the media resource of an entity.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityMedia(DeleteUriInfo uriInfo, String contentType) throws ODataException;
}
