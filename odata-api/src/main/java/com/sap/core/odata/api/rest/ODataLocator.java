package com.sap.core.odata.api.rest;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;

/**
 * JAX-RS locator handling OData protocol
 */
public interface ODataLocator {

  /**
   * initialze OData locator before request handling
   */
  void beforRequest() throws ODataError;

  void setPathSegments(List<PathSegment> odataPathSegments);

  void setHttpHeaders(HttpHeaders httpHeaders);

  void setUriInfo(UriInfo uriInfo);

  void setProcessor(ODataProcessor processor);

}