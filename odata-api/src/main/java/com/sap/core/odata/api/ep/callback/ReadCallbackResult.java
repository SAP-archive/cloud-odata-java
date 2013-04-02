package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;

public class ReadCallbackResult {

  private EntityProviderReadProperties properties;
  private EdmEntitySet entitySet;

  public ReadCallbackResult(final EntityProviderReadProperties properties, final EdmEntitySet entitySet) {
    super();
    this.properties = properties;
    this.entitySet = entitySet;
  }

  public EntityProviderReadProperties getConsumerProperties() {
    return properties;
  }

  public EdmEntitySet getEntitySet() {
    return entitySet;
  }

}
