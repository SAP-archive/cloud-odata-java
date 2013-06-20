package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

/**
 * @author SAP AG
 */
public interface ODataRequestHandlerInterface {
  /**
   * <p>Handles the {@link BatchPart} in a way that it results in a corresponding {@link ODataResponse}.</p>
   * @param batchPart the incoming batchPart
   * @return the corresponding result
   * @throws ODataException
   */
  public ODataResponse handle(BatchPart batchPart) throws ODataException;

  /**
   * <p>Handles the {@link ODataRequest} in a way that it results in a corresponding {@link ODataResponse}.</p>
   * <p>This includes building of the {@link com.sap.core.odata.api.processor.ODataContext ODataContext},
   * delegation of URI parsing and dispatching of the request internally.</p>
   * @param request the incoming request
   * @return the corresponding result
   * @throws ODataException
   */
  public ODataResponse handle(ODataRequest request);
}
