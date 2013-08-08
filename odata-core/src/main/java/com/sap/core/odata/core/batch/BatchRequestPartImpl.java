package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.batch.BatchRequestPart;
import com.sap.core.odata.api.processor.ODataRequest;

public class BatchRequestPartImpl implements BatchRequestPart {

  private List<ODataRequest> requests = new ArrayList<ODataRequest>();
  private boolean isChangeSet;

  public BatchRequestPartImpl() {}

  public BatchRequestPartImpl(final boolean isChangeSet, final List<ODataRequest> requests) {
    this.isChangeSet = isChangeSet;
    this.requests = requests;
  }

  @Override
  public boolean isChangeSet() {
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
