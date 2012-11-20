package com.sap.core.odata.api.processor;

import java.util.Map;

/**
 * URI path segment consisting of URI path element and URI matrix parameters. Example:
 * <code>
 *  //moremaps.com/map/color;lat=50;long=20;scale=32000
 * <code>
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
   * Returns a readonly map of matrix parameters. In the example above, lat, long, and scale are matrix parameters.
   * @return a read only map of matrix parameters and their values
   */
  Map<String, String> getMatrixParameters();

}
