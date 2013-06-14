package com.sap.core.odata.api.ep;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.uri.PathInfo;

public class EntityProviderBatchProperties {

  private PathInfo pathInfo;
  private ODataServiceFactory serviceFactory;
  
  public static EntityProviderBatchPropertiesBuilder init() {
    return new  EntityProviderBatchPropertiesBuilder ();
  }

  public PathInfo getPathInfo() {
    return pathInfo;
  }

  public ODataServiceFactory getServiceFactory() {
    return serviceFactory;
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
    
    public EntityProviderBatchPropertiesBuilder serviceFactory(final ODataServiceFactory serviceFactory) {
      properties.serviceFactory = serviceFactory;
      return this;
    }

    public EntityProviderBatchProperties build() {
      return properties;
    }
  }

}
