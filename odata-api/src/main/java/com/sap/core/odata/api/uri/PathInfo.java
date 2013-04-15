/**
 * (c) 2013 by SAP AG
 */
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

}
