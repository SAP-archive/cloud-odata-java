package com.sap.core.odata.api.processor;

import com.sap.core.odata.api.exception.ODataException;

/**
 * Execute a OData batch request. 
 * 
 * @author SAP AG
 *
 */
public interface BatchProcessor extends ODataProcessor {

  /**
   * @param contentType 
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeBatch(String contentType) throws ODataException;
}
