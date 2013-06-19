package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;

public interface ODataRequestHandlerInterface {

  public ODataResponse handle(BatchPart batchPart) throws ODataException;

  public ODataResponse handle(ODataRequest request) throws ODataException;
}
