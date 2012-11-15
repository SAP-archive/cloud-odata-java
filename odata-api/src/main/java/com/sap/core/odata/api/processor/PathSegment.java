package com.sap.core.odata.api.processor;

import java.util.Map;

/**
 * URI path segement consisting of uri path element and uri matrix parameter. Example:
 * <code>
 *  //moremaps.com/map/color;lat=50;long=20;scale=32000
 * <code>
 * 
 * @author SAP AG
 */
public interface PathSegment {

  /**
   * Returns a uri path element. In the example above 'color' will be an element.
   * @return a uri path element
   */
  String getPath();

  /**
   * Returns a readonly map of matrix parameter. In the example above lat, long and scale are matrix paramters.
   * @return a read only map of matrix parameter and their values
   */
  Map<String, String> getMatrixParameters();

}
