package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.uri.PathInfo;

/**
 * The {@link EntityProviderBatchProperties} contains necessary informations to parse a Batch Request body.
 * 
 * @author SAP AG
 */
public class EntityProviderBatchProperties {
  /**
   * PathInfo contains service root and preceding segments which should be used for URI parsing of a single request
   */
  private PathInfo pathInfo;

  public static EntityProviderBatchPropertiesBuilder init() {
    return new EntityProviderBatchPropertiesBuilder();
  }

  public PathInfo getPathInfo() {
    return pathInfo;
  }

  public static class EntityProviderBatchPropertiesBuilder {
    private final EntityProviderBatchProperties properties = new EntityProviderBatchProperties();

    public EntityProviderBatchPropertiesBuilder() {}

    public EntityProviderBatchPropertiesBuilder(final EntityProviderBatchProperties propertiesFrom) {
      properties.pathInfo = propertiesFrom.pathInfo;
    }

    public EntityProviderBatchPropertiesBuilder pathInfo(final PathInfo pathInfo) {
      properties.pathInfo = pathInfo;
      return this;
    }

    public EntityProviderBatchProperties build() {
      return properties;
    }
  }

}
