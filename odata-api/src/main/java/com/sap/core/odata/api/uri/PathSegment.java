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

import java.util.List;
import java.util.Map;

/**
 * <p>URI path segment consisting of an URI path element and URI matrix parameters.</p>
 * <p>Example URI:
 * <pre>{@code //moremaps.com/map/color;mul=50,25;long=20;scale=32000}</pre>
 * where <code>color</code> is a path segment,
 * <code>mul</code>, <code>long</code>, and <code>scale</code> are matrix parameters,
 * and the matrix parameter <code>mul</code> has a multi-value list.</p> 
 *
 * @com.sap.core.odata.DoNotImplement
 * @author SAP AG
 */
public interface PathSegment {

  /**
   * Gets a URI path element. In the example above, <code>color</code> will be an element.
   * @return a URI path element
   */
  String getPath();

  /**
   * <p>Gets the matrix parameters of this path segment.</p>
   * <p>In the {@link PathSegment} example, <code>mul</code>, <code>long</code>,
   * and <code>scale</code> are matrix parameters.</p>
   * @return an immutable map of matrix parameters and their values
   */
  Map<String, List<String>> getMatrixParameters();

}
