package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
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
public interface Entity extends ProcessorFeature {

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntity(GetEntityUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse existsEntity(GetEntityCountUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param request 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntity(PutMergePatchUriInfo uriInfo, ODataRequest request, String contentType) throws ODataException;

  /**
   * @param uriInfo  a {@link DeleteUriInfo} object with information from the URI parser
   * @param contentType 
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntity(DeleteUriInfo uriInfo, String contentType) throws ODataException;

}
