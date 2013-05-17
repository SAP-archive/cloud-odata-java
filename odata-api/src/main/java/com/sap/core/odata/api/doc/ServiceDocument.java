package com.sap.core.odata.api.doc;

import java.util.List;

import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.ep.EntityProviderException;

public interface ServiceDocument {
  public List<EdmEntitySetInfo> getEntitySetsInfo() throws EntityProviderException;
}
