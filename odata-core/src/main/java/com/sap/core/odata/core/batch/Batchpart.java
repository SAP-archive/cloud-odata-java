package com.sap.core.odata.core.batch;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ODataRequestHandler;

public interface Batchpart {

  public void process(ODataRequestHandler requestHandler, BatchWriter batchWriter, String boundary) throws EntityProviderException;

  public ODataResponse processWithResponse(final ODataRequestHandler requestHandler, BatchWriter batchWriter) throws EntityProviderException;
}
