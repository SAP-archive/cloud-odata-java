package com.sap.core.odata.api.batch;

import java.util.List;

/**
 * A Batch Result
 * <p>Batch Result represents a result after parsing of the batch request body
 * @author SAP AG
 */
public interface BatchResult {
  /**
   * Get distinct MIME parts of the Batch Request
   * @return a list of {@link BatchParts}
   */
  public List<BatchPart> getBatchParts();
}
