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

  private final EntityProviderReadProperties properties;
  private final EdmEntitySet entitySet;
  private final String navigationPropertyName;

  /**
   * Result of a read callback.
   * 
   * <code>NULL</code> parameters are not allowed and leading to a <code>NPE</code>.
   * 
   * @param properties the {@link EntityProviderReadProperties} for general entity provider read settings.
   * @param entitySet an {@link EdmEntitySet} which contains the Edm information about the inlined navigation property entity.
   * @param navigationPropertyName the name of the navigation property which points to this entity/entities
   */
  public ReadCallbackResult(final EntityProviderReadProperties properties, final EdmEntitySet entitySet, final String navigationPropertyName) {
    super();
    this.properties = properties;
    this.entitySet = entitySet;
    this.navigationPropertyName = navigationPropertyName;
  }

  public EntityProviderReadProperties getConsumerProperties() {
    return properties;
  }

  public EdmEntitySet getEntitySet() {
    return entitySet;
  }

  public String getNavigationPropertyName() {
    return navigationPropertyName;
  }
}
