package com.sap.core.odata.core.rest;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

/**
 * Combination of generic http objects. 
 */
public interface ODataContext {

  /**
   * @return OData path segments 
   */
  List<PathSegment> getPathSegments();

  /**
   * @return raw http headers
   */
  HttpHeaders getHttpHeaders();

  /** 
   * @return raw uri infor
   */
  UriInfo getUriInfo();

  /** 
   * @return http request object
   */
  Request getRequest();
  
}