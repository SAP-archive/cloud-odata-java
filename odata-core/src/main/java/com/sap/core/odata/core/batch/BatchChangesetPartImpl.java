package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.batch.BatchChangesetPart;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataRequest;

public class BatchChangesetPartImpl implements BatchChangesetPart {
  private List<ODataRequest> requests = new ArrayList<ODataRequest>();

  @Override
  public boolean isChangeSet() throws EntityProviderException {
    return true;
  }

  @Override
  public List<ODataRequest> getRequests() {
    return Collections.unmodifiableList(requests);
  }

  public void setRequests(final List<ODataRequest> requests) {
    this.requests = requests;
  }

}
