package com.sap.core.odata.core.rest;

import java.util.List;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.rest.ODataContext;


public class ODataContextImpl implements ODataContext {

  private static final Logger log = LoggerFactory.getLogger(ODataContextImpl.class);


  private UriInfo uriInfo;
  private Request request;
  private HttpHeaders httpHeaders;
  List<PathSegment> pathSegments;

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

  public HttpHeaders getHttpHeaders() {
    return httpHeaders;
  }

  public void setHttpHeaders(HttpHeaders httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public List<PathSegment> getPathSegments() {
    return pathSegments;
  }

  public void setPathSegments(List<PathSegment> pathSegments) {
    this.pathSegments = pathSegments;
  }

  public void log() {
    ODataContextImpl.log.debug("--odata http context----------------");
    ODataContextImpl.log.debug("odataPath:      " + this.pathSegments);
    ODataContextImpl.log.debug("httpHeaders:    " + this.httpHeaders.getRequestHeaders());
    ODataContextImpl.log.debug("request method: " + this.request.getMethod());
    ODataContextImpl.log.debug("----------------------------------");
    ODataContextImpl.log.debug("uriInfo:        " + this.uriInfo.getPath());
    ODataContextImpl.log.debug("baseUri:        " + this.uriInfo.getBaseUri());
    ODataContextImpl.log.debug("queryParam:     " + this.uriInfo.getQueryParameters());
    ODataContextImpl.log.debug("pathParam:      " + this.uriInfo.getPathParameters());
    ODataContextImpl.log.debug("pathSegments:   " + this.uriInfo.getPathSegments());
    ODataContextImpl.log.debug("----------------------------------");
  }


}
