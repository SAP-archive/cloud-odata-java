package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.processor.ODataRequest;

public class BatchPartImpl implements BatchPart {

  private List<ODataRequest> requests = new ArrayList<ODataRequest>();
  private boolean isChangeSet;

  public BatchPartImpl() {}

  public BatchPartImpl(final boolean isChangeSet, final List<ODataRequest> requests) {
    this.isChangeSet = isChangeSet;
    this.requests = requests;
  }

  @Override
  public boolean isChangeSet() throws EntityProviderException {
    return isChangeSet;
  }

  public void setChangeSet(final boolean isChangeSet) {
    this.isChangeSet = isChangeSet;
  }

  @Override
  public List<ODataRequest> getRequests() {
    return Collections.unmodifiableList(requests);
  }

  public void setRequests(final List<ODataRequest> requests) {
    this.requests = requests;
  }

}
