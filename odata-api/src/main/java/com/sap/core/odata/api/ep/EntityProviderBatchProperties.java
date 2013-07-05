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
