package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.uri.PathInfo;

public class EntityProviderBatchProperties {

  private PathInfo pathInfo;
  
  public static EntityProviderBatchPropertiesBuilder init() {
    return new  EntityProviderBatchPropertiesBuilder ();
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
