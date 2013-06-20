package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.processor.ODataRequest;

public interface BatchQueryPart extends BatchPart {
  public ODataRequest getRequest();
}
