package com.sap.core.odata.api.processor.feature;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * Execute a OData batch request. 
 * 
 * @author SAP AG
 *
 */
public interface Batch extends ProcessorFeature {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeBatch(String contentType) throws ODataException;
}
