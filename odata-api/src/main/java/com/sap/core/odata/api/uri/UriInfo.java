package com.sap.core.odata.api.uri;

import java.net.URI;
import java.util.List;


/**
 * Object to keep OData URI information. 
 * 
 * @author SAP AG
 */
public interface UriInfo {

  /**
   * Returns preceding path segments  
   * @return list of path segments
   */
  List<PathSegment> getPrecedingPathSegmentList();

  /**
   * Returns OData path segments as immutable list  
   * @return list of path segments
   */
  List<PathSegment> getODataPathSegmentList();

  /**
   * @return absolute base uri of the request
   */
  URI getBaseUri();
  
}
