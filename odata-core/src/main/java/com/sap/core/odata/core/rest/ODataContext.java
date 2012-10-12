package com.sap.core.odata.core.rest;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public interface ODataContext {

  List<PathSegment> getPathSegments();

  HttpHeaders getHttpHeaders();

  UriInfo getUriInfo();

  Request getRequest();

}