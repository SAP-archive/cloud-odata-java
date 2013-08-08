package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * @author SAP AG
 */
public interface BatchHandler {
  /**
   * <p>Handles the {@link BatchRequestPart} in a way that it results in a corresponding {@link BatchResponsePart}.</p>
   * @param batchRequestPart the incoming MIME part
   * @return the corresponding result
   * @throws ODataException
   */
  public BatchResponsePart handleBatchPart(BatchRequestPart batchRequestPart) throws ODataException;

  /**
   * <p>Delegates a handling of the request {@link ODataRequest} to the request handler and provides ODataResponse {@link ODataResponse}.</p>
   * @param request the incoming request
   * @return the corresponding result
   * @throws ODataException
   */
  public ODataResponse handleRequest(ODataRequest request) throws ODataException;
}
