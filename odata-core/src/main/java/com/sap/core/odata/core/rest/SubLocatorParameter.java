/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Request;

import com.sap.core.odata.api.ODataServiceFactory;

/**
 * @author SAP AG
 */
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
