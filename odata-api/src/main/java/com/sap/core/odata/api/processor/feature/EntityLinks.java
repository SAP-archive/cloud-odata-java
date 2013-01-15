package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksCountUriInfo;
import com.sap.core.odata.api.uri.info.GetEntitySetLinksUriInfo;
import com.sap.core.odata.api.uri.info.PostUriInfo;

/**
 * Execute a OData entity links request. 
 * 
 * @author SAP AG
 */
public interface EntityLinks extends ProcessorFeature {
  /**
   * @param contentType 
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse readEntityLinks(GetEntitySetLinksUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse countEntityLinks(GetEntitySetLinksCountUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param request 
   * @return a odata response object
   * @throws ODataException
   */
  ODataResponse createEntityLink(PostUriInfo uriInfo, Object content, String contentType) throws ODataException;
}
