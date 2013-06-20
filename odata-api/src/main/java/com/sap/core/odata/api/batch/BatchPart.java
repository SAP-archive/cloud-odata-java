package com.sap.core.odata.api.batch;

import com.sap.core.odata.api.ep.EntityProviderException;

public interface BatchPart {
  public boolean isChangeSet() throws EntityProviderException;
}
