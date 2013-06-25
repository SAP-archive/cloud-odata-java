package com.sap.core.odata.core.batch;

import java.util.List;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.batch.BatchHandler;
import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.part.BatchProcessor;
import com.sap.core.odata.core.ODataRequestHandler;

public class BatchHandlerImpl implements BatchHandler {
  private ODataServiceFactory factory;
  private BatchProcessor processor;

  public BatchHandlerImpl(final ODataServiceFactory factory, final BatchProcessor processor) {
    this.factory = factory;
    this.processor = processor;
  }

  @Override
  public ODataResponse handleBatchPart(final BatchPart batchPart) throws ODataException {
    ODataResponse response;
    if (batchPart.isChangeSet()) {
      List<ODataRequest> changeSetRequests = batchPart.getRequests();
      response = processor.executeChangeSet(this, changeSetRequests);
    } else {
      if (batchPart.getRequests().size() != 1) {
        throw new ODataException("Query Operation should contain one request");
      }
      ODataRequestHandler handler = new ODataRequestHandler(factory);
      response = handler.handle(batchPart.getRequests().get(0));
    }
    return response;
  }

  @Override
  public ODataResponse handleRequest(final ODataRequest request) {
    ODataRequestHandler handler = new ODataRequestHandler(factory);
    return handler.handle(request);
  }
}
