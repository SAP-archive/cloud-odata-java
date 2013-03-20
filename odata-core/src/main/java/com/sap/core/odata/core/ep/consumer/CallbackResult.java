package com.sap.core.odata.core.ep.consumer;

import com.sap.core.odata.api.edm.EdmEntitySet;

public class CallbackResult {

  private ConsumerProperties properties;
  private EdmEntitySet entitySet;

  public CallbackResult(ConsumerProperties properties, EdmEntitySet entitySet) {
    super();
    this.properties = properties;
    this.entitySet = entitySet;
  }

  public ConsumerProperties getConsumerProperties() {
    return properties;
  }

  public EdmEntitySet getEntitySet() {
    return entitySet;
  }

}
