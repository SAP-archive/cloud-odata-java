package com.sap.core.odata.core.doc;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.doc.ServiceDocument;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.ep.EntityProviderException;

public class ServiceDocumentImpl implements ServiceDocument {
  private List<EdmEntitySetInfo> entitySets = new ArrayList<EdmEntitySetInfo>();

  public ServiceDocumentImpl setEntitySetsInfo(final List<EdmEntitySetInfo> entitySets) {
    this.entitySets = entitySets;
    return this;
  }

  @Override
  public List<EdmEntitySetInfo> getEntitySetsInfo() throws EntityProviderException {
    return entitySets;
  }

}
