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
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse readEntitySimplePropertyValue(GetSimplePropertyUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse updateEntitySimplePropertyValue(PutMergePatchUriInfo uriInfo, String contentType) throws ODataException;

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse deleteEntitySimplePropertyValue(DeleteUriInfo uriInfo, String contentType) throws ODataException;
}
