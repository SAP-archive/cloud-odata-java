package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.batch.BatchHandler;
import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ODataContextImpl;
import com.sap.core.odata.core.ODataRequestHandler;

public class BatchHandlerImpl implements BatchHandler {
  private ODataServiceFactory factory;
  private ODataService service;

  public BatchHandlerImpl(final ODataServiceFactory factory, final ODataService service) {
    this.factory = factory;
    this.service = service;
  }

  @Override
  public BatchResponsePart handleBatchPart(final BatchPart batchPart) throws ODataException {
    if (batchPart.isChangeSet()) {
      List<ODataRequest> changeSetRequests = batchPart.getRequests();
      return service.getBatchProcessor().executeChangeSet(this, changeSetRequests);
    } else {
      if (batchPart.getRequests().size() != 1) {
        throw new ODataException("Query Operation should contain one request");
      }
      ODataRequest request = batchPart.getRequests().get(0);
      ODataRequestHandler handler = createHandler(request);
      ODataResponse response = handler.handle(request);
      List<ODataResponse> responses = new ArrayList<ODataResponse>(1);
      responses.add(response);
      return BatchResponsePart.responses(responses).changeSet(false).build();
    }
  }

  @Override
  public ODataResponse handleRequest(final ODataRequest request) throws ODataException {
    ODataRequestHandler handler = createHandler(request);
    return handler.handle(request);
  }

  private ODataRequestHandler createHandler(final ODataRequest request) throws ODataException {
    ODataContextImpl context = new ODataContextImpl(request, factory);

    ODataContext parentContext = service.getProcessor().getContext();
    context.setBatchParentContext(parentContext);

    context.setService(service);
    service.getProcessor().setContext(context);

    return new ODataRequestHandler(factory, service, context);
  }

}
