package com.sap.core.odata.api.batch;

import java.util.List;

import com.sap.core.odata.api.processor.ODataRequest;

/**
 * A BatchPart
 * <p> BatchPart represents a distinct MIME part of a Batch Request body. It can be ChangeSet or Query Operation
 * @author SAP AG
 */
public interface BatchPart {

  /**
   * Get the info if a BatchPart is a ChangeSet
   * @return true or false
   */
  public boolean isChangeSet();

  /**
   * Get requests. If a BatchPart is a Query Operation, the list contains one request.
   * @return a list of {@link ODataRequest}
   */
  public List<ODataRequest> getRequests();
}
