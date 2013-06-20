package com.sap.core.odata.api.processor.part;

import java.io.InputStream;
import java.util.List;

import com.sap.core.odata.api.batch.ODataRequestHandlerInterface;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * Execute a OData batch request. 
 * 
 * @author SAP AG
 *
 */
public interface BatchProcessor extends ODataProcessor {

  /**
   * Executes a OData batch request and provide Batch Response as {@link ODataResponse} 
   * @param handler handler that will handle the requests
   * @param contentType the content type of the request
   * @param content Batch Request body
   * @return a {@link ODataResponse} object
   * @throws ODataException
   */
  ODataResponse executeBatch(ODataRequestHandlerInterface handler, String contentType, InputStream content) throws ODataException;

  /**
   * Executes a Change Set and provide Change Set Response as {@link ODataResponse}.
   * The method has to define a rollback semantic that may be applied when a request within a Change Set fails (all-or-nothing requirement).
   * If a request within a Change Set fails, instead of Change Set Response should be returned the error response 
   * @param handler handler that will handle each single change request
   * @param requests list of single change requests
   * @return a {@link ODataResponse} object
   * @throws ODataException 
   */
  ODataResponse executeChangeSet(ODataRequestHandlerInterface handler, List<ODataRequest> requests) throws ODataException;
}
