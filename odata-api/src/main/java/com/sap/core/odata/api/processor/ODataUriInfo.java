package com.sap.core.odata.api.processor;

import java.net.URI;
import java.util.List;

import com.sap.core.odata.api.uri.PathSegment;

/**
 * Object to keep OData URI information. 
 * 
 * @author SAP AG
 */
public interface ODataUriInfo {

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
