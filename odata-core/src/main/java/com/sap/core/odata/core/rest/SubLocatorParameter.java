package com.sap.core.odata.core.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Request;

import com.sap.core.odata.api.ODataServiceFactory;

public class SubLocatorParameter {

  private List<javax.ws.rs.core.PathSegment> pathSegments;
  private javax.ws.rs.core.HttpHeaders httpHeaders;
  private javax.ws.rs.core.UriInfo uriInfo;
  private Request request;
  private int pathSplit;
  private ODataServiceFactory serviceFactory;
  private HttpServletRequest servletRequest;

  public ODataServiceFactory getServiceFactory() {
    return serviceFactory;
  }

  public void setServiceFactory(final ODataServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  public List<javax.ws.rs.core.PathSegment> getPathSegments() {
    return pathSegments;
  }

  public void setPathSegments(final List<javax.ws.rs.core.PathSegment> pathSegments) {
    this.pathSegments = pathSegments;
  }

  public javax.ws.rs.core.HttpHeaders getHttpHeaders() {
    return httpHeaders;
  }

  public void setHttpHeaders(final javax.ws.rs.core.HttpHeaders httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public javax.ws.rs.core.UriInfo getUriInfo() {
    return uriInfo;
  }

  public void setUriInfo(final javax.ws.rs.core.UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  public Request getRequest() {
    return request;
  }

  public void setRequest(final Request request) {
    this.request = request;
  }

  public int getPathSplit() {
    return pathSplit;
  }

  public void setPathSplit(final int pathSplit) {
    this.pathSplit = pathSplit;
  }

  public void setServletRequest(final HttpServletRequest servletRequest) {
    this.servletRequest = servletRequest;
  }

  public HttpServletRequest getServletRequest() {
    return servletRequest;
  }
}
