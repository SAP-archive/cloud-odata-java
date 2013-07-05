package com.sap.core.odata.core.batch;

import java.util.List;

import com.sap.core.odata.api.batch.BatchResponsePart;
import com.sap.core.odata.api.processor.ODataResponse;

public class BatchResponsePartImpl extends BatchResponsePart {
  private List<ODataResponse> responses;
  private boolean isChangeSet;

  @Override
  public List<ODataResponse> getResponses() {
    return responses;
  }

  @Override
  public boolean isChangeSet() {
    return isChangeSet;
  }

  public class BatchResponsePartBuilderImpl extends BatchResponsePartBuilder {
    private List<ODataResponse> responses;
    private boolean isChangeSet;

    @Override
    public BatchResponsePart build() {
      BatchResponsePartImpl.this.responses = responses;
      BatchResponsePartImpl.this.isChangeSet = isChangeSet;
      return BatchResponsePartImpl.this;
    }

    @Override
    public BatchResponsePartBuilder responses(final List<ODataResponse> responses) {
      this.responses = responses;
      return this;
    }

    @Override
    public BatchResponsePartBuilder changeSet(final boolean isChangeSet) {
      this.isChangeSet = isChangeSet;
      return this;
    }
  }
}
