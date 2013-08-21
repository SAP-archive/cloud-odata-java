package com.sap.core.odata.api.client.batch;

import java.util.List;

import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * A BatchChangeSet
 * <p> BatchChangeSet represents a Change Set, that consists of change requests
 *
 * @author SAP AG
 */
public abstract class BatchChangeSet implements BatchPart {

  /**
   * Add a new change request to the ChangeSet
   * @param BatchChangeSetPart {@link BatchChangeSetPart}
   */
  public abstract void add(BatchChangeSetPart request);

  /**
   * Get change requests 
   * @return a list of {@link BatchChangeSetPart}
   */
  public abstract List<BatchChangeSetPart> getChangeSetParts();

  /**
   * Get new builder instance 
   * @return {@link BatchChangeSetBuilder}
   */
  public static BatchChangeSetBuilder newBuilder() {
    return BatchChangeSetBuilder.newInstance();
  }

  public static abstract class BatchChangeSetBuilder {

    protected BatchChangeSetBuilder() {}

    private static BatchChangeSetBuilder newInstance() {
      return RuntimeDelegate.createBatchChangeSetBuilder();
    }

    public abstract BatchChangeSet build();
  }
}
