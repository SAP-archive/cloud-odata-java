package com.sap.core.odata.api.processor;

import java.util.List;

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
  List<ODataPathSegment> getPrecedingPathSegmentList();

  /**
   * Returns OData path segments as immutable list  
   * @return list of path segments
   */
  List<ODataPathSegment> getODataPathSegmentList();

  /**
   * @return absolute base uri of the request
   */
  String getBaseUri();

  
  
}
