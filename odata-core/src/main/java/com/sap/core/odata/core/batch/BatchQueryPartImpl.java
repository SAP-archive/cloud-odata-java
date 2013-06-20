package com.sap.core.odata.core.batch;

import com.sap.core.odata.api.batch.BatchQueryPart;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataRequest;

public class BatchQueryPartImpl implements BatchQueryPart {
  private ODataRequest request;

  @Override
  public ODataRequest getRequest() {
    return request;
  }

  public void setRequest(final ODataRequest request) {
    this.request = request;

  }

  @Override
  public boolean isChangeSet() throws EntityProviderException {
    return false;

  }

}
