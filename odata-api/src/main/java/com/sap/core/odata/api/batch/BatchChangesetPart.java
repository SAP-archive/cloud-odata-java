package com.sap.core.odata.api.batch;

import java.util.List;

import com.sap.core.odata.api.processor.ODataRequest;

public interface BatchChangesetPart extends BatchPart {
  public List<ODataRequest> getRequests();
}
