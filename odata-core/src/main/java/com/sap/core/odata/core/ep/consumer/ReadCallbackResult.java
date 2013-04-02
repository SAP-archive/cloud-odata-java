package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.edm.EdmEntitySet;

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
