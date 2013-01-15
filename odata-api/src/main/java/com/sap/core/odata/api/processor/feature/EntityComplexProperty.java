package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetComplexPropertyUriInfo;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;

/**
 * Execute a OData complex property request. 
 * 
 * @author SAP AG
 */
public interface EntityComplexProperty extends ProcessorFeature {
  /**
   * Reads a complex property of an entity.
   * @param uriInfo information about the request URI
   * @param contentType the content type of the response
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityComplexProperty(GetComplexPropertyUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * Updates a complex property of an entity.
   * @param uriInfo information about the request URI
   * @param content the content of the request, containing the updated property data
   * @param requestContentType the content type of the request body
   * @param merge if <code>true</code>, properties not present in the data are left unchanged;
   *              if <code>false</code>, they are reset
   * @param contentType the content type of the response
   * @param request 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityComplexProperty(PutMergePatchUriInfo uriInfo, Object content, String requestContentType, boolean merge, String contentType) throws ODataException;
}
