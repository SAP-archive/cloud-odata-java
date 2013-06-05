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
package com.sap.core.odata.api.uri;

import java.net.URI;
import java.util.List;

/**
 * Object to keep OData URI information.
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface PathInfo {

  /**
   * Gets preceding path segments.
   * @return List of path segments
   */
  List<PathSegment> getPrecedingSegments();

  /**
   * Gets OData path segments as immutable list.
   * @return List of path segments
   */
  List<PathSegment> getODataSegments();

  /**
   * Gets the root URI of this service.
   * @return absolute base URI of the request
   */
  URI getServiceRoot();

  /**
   * Get the absolute request URI including any query parameters.
   * @return the absolute request URI
   */
  URI getRequestUri();

}
