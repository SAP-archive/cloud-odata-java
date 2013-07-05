package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * @author SAP AG
 */
public interface BatchHandler {
  /**
   * <p>Handles the {@link BatchPart} in a way that it results in a corresponding {@link BatchResponsePart}.</p>
   * @param batchPart the incoming batchPart
   * @return the corresponding result
   * @throws ODataException
   */
  public BatchResponsePart handleBatchPart(BatchPart batchPart) throws ODataException;

  /**
   * <p>Delegates a handling of the request {@link ODataRequest} to the request handler and provides ODataResponse {@link ODataResponse}.</p>
   * @param request the incoming request
   * @return the corresponding result
   * @throws ODataException
   */
  public ODataResponse handleRequest(ODataRequest request) throws ODataException;
}
