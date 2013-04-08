package com.sap.core.odata.api.ep.callback;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;

/**
 * Result of a read callback.
 * It must contains at an {@link EdmEntitySet} which contains the Edm information about the inlined navigation property entity
 * and the {@link EntityProviderReadProperties} for general entity provider read settings.
 * 
 * @author SAP AG
 */
public class ReadCallbackResult {

  private EntityProviderReadProperties properties;
  private EdmEntitySet entitySet;

  /**
   * Result of a read callback.
   * 
   * <code>NULL</code> parameters are not allowed and leading to a <code>NPE</code>.
   * 
   * @param properties the {@link EntityProviderReadProperties} for general entity provider read settings.
   * @param entitySet an {@link EdmEntitySet} which contains the Edm information about the inlined navigation property entity.
   */
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
