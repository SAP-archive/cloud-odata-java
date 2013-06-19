package com.sap.core.odata.core.batch;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.batch.BatchPart;
import com.sap.core.odata.api.batch.BatchResult;

public class BatchResultImpl implements BatchResult {
  private List<BatchPart> batchParts = new ArrayList<BatchPart>();

  @Override
  public List<BatchPart> getBatchParts() {
    return batchParts;
  }

  public void setBatchParts(final List<BatchPart> batchParts) {
    this.batchParts = batchParts;
  }

}
