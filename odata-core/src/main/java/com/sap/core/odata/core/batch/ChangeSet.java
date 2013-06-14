package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ODataRequestHandler;

public class ChangeSet implements Batchpart {
  private List<BatchRequest> batchRequests = new ArrayList<BatchRequest>();

  public List<BatchRequest> getBatchRequests() {
    return Collections.unmodifiableList(batchRequests);
  }

  public void setBatchRequests(final List<BatchRequest> requests) {
    batchRequests = requests;
  }

  @Override
  public ODataResponse processWithResponse(final ODataRequestHandler requestHandler, final BatchWriter batchWriter) throws EntityProviderException {
    List<ODataResponse> responses = new ArrayList<ODataResponse>();
    for (BatchRequest batchRequest : batchRequests) {
      // ChangeSet Callback
      responses.add(batchRequest.processWithResponse(requestHandler, batchWriter));
      // ChangeSet Callback
    }
    return batchWriter.writeChangeSet(responses);
  }

  @Override
  public void process(final ODataRequestHandler requestHandler, final BatchWriter batchWriter, final String boundary) throws EntityProviderException {
    String changeSetBoundary = "changeset_" + UUID.randomUUID().toString();
    batchWriter.appendDelimiter(boundary);
    batchWriter.appendChangeSet(changeSetBoundary);

    for (BatchRequest batchRequest : batchRequests) {
      // ChangeSet Callback
      batchRequest.process(requestHandler, batchWriter, changeSetBoundary);
      // ChangeSet Callback
    }
    batchWriter.appendCloseDelimiter(changeSetBoundary);
  }

}
