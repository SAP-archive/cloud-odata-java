package com.sap.core.odata.core.rest;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class ODataContext {

  private UriInfo uriInfo;
  private Request request;
  private HttpHeaders httpHeaders;
  List<PathSegment> pathSegments;

  public List<PathSegment> getPathSegments() {
    return pathSegments;
  }

  public void setPathSegments(List<PathSegment> pathSegments) {
    this.pathSegments = pathSegments;
  }

  public HttpHeaders getHttpHeaders() {
    return httpHeaders;
  }

  public void setHttpHeaders(HttpHeaders httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public void setUriInfo(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

}
