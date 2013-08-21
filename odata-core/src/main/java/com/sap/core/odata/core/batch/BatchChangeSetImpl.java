package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.core.odata.api.client.batch.BatchChangeSet;
import com.sap.core.odata.api.client.batch.BatchChangeSetPart;

public class BatchChangeSetImpl extends BatchChangeSet {
  private List<BatchChangeSetPart> requests = new ArrayList<BatchChangeSetPart>();

  @Override
  public void add(final BatchChangeSetPart request) {
    requests.add(request);
  }

  @Override
  public List<BatchChangeSetPart> getChangeSetParts() {
    return Collections.unmodifiableList(requests);
  }

  public class BatchChangeSetBuilderImpl extends BatchChangeSetBuilder {

    @Override
    public BatchChangeSet build() {
      return BatchChangeSetImpl.this;
    }

  }
}
