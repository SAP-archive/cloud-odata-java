package com.sap.core.odata.api.uri;

import java.net.URI;
import java.util.List;

/**
 * Object to keep OData URI information. 
 * 
 * @author SAP AG
 */
public interface PathInfo {

  /**
   * Returns preceding path segments  
   * @return list of path segments
   */
  List<PathSegment> getPrecedingSegments();

  /**
   * Returns OData path segments as immutable list  
   * @return list of path segments
   */
  List<PathSegment> getODataSegments();

  /**
   * @return absolute base uri of the request
   */
  URI getServiceRoot();

}
