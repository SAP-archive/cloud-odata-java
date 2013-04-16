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
