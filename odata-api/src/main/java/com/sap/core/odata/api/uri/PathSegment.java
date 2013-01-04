package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

/**
 * URI path segment consisting of URI path element and URI matrix parameters. Example uri:
 * <pre>
 * {@code
 *  //moremaps.com/map/color;mul=50,25;long=20;scale=32000
 * }
 * </pre>
 * 'color' is a path segment 'mul', 'long' and 'scale' are matrix parameters, where 'mul' has a multi value list. 
 * 
 * @author SAP AG
 */
public interface PathSegment {

  /**
   * Returns a URI path element. In the example above, 'color' will be an element.
   * @return a URI path element
   */
  String getPath();

  /**
   * Returns a read only map of matrix parameters. In the example above, mul, long, and scale are matrix parameters.
   * @return a read only map of matrix parameters and their values
   */
  Map<String, List<String>> getMatrixParameters();

}
