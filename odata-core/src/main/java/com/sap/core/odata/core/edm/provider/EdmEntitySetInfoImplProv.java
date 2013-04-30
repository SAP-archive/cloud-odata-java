/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

  public EdmEntitySetInfoImplProv(final EntitySet entitySet, final EntityContainerInfo entityContainerInfo) throws EdmException {
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
