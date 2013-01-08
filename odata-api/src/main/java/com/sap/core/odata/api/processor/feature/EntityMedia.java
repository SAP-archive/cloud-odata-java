package com.sap.core.odata.api.processor.feature;

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
   * @param contentType
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntityMedia(GetMediaResourceUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntityMedia(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType
   * @return an {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntityMedia(DeleteUriInfo uriInfo, String contentType) throws ODataException;
}
