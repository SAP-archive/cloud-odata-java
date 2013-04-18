package com.sap.core.odata.core.edm.provider;

import java.net.URI;
import java.net.URISyntaxException;

import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;

public class EdmEntitySetInfoImplProv implements EdmEntitySetInfo {

  private final String entitySetName;
  private final URI entitySetUri;
  private final String entityContainerName;
  private final boolean isDefaultEntityContainer;

  public EdmEntitySetInfoImplProv(EntitySet entitySet, EntityContainerInfo entityContainerInfo) throws EdmException {
    entityContainerName = entityContainerInfo.getName();
    isDefaultEntityContainer = entityContainerInfo.isDefaultEntityContainer();

    entitySetName = entitySet.getName();

    try {
      if (isDefaultEntityContainer) {
        entitySetUri = new URI(entitySetName);
      } else {
        entitySetUri = new URI(entityContainerName + "." + entitySetName);
      }
    } catch (URISyntaxException e) {
      throw new EdmException(EdmException.COMMON, e);
    }

  }

  @Override
  public String getEntityContainerName() {
    return entityContainerName;

  }

  @Override
  public String getEntitySetName() {
    return entitySetName;

  }

  @Override
  public boolean isDefaultEntityContainer() {
    return isDefaultEntityContainer;

  }

  @Override
  public URI getEntitySetUri() {
    return entitySetUri;

  }

}
