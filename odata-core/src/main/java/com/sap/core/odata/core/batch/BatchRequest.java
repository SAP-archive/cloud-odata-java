package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ODataRequestHandler;

public class BatchRequest implements Batchpart {
  private ODataRequest odataRequest;
  private List<ODataRequest> requests = new ArrayList<ODataRequest>();

  public ODataRequest getOdataRequest() {
    return odataRequest;
  }

  public void setOdataRequest(final ODataRequest odataRequest) {
    this.odataRequest = odataRequest;
    requests.add(odataRequest);
  }

  @Override
  public void process(final ODataRequestHandler requestHandler, final BatchWriter batchWriter, final String boundary) throws EntityProviderException {
    ODataResponse response;
    try {
      response = requestHandler.handle(getOdataRequest());

      batchWriter.appendDelimiter(boundary);
      batchWriter.appendResponse(response);
    } catch (ODataException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  /*@Override
  public ODataResponse process(final ODataRequestHandler requestHandler) throws EntityProviderException {
    ODataResponse response;
    try {
     // response = BatchWriter.writeResponse(requestHandler.handle(getOdataRequest()));
    } catch (ODataException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
    return response;
  }*/
}
