package com.sap.core.odata.api.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;

/**
 * Default OData root locator responsible to handle the whole path and delegate all calls to a sub locator:<p>
 * <code>/{odata path}  e.g. http://host:port/webapp/odata.svc/$metadata</code><br>
 * All path segments defined by a servlet mapping belong to the odata uri.
 * </p>
 * This behavior can be changed:<p>
 * <code>/{custom path}{odata path}  e.g. http://host:port/webapp/bmw/odata.svc/$metadata</code><br>
 * The first segment defined by a servlet mapping belong to customer context and the following segments are OData specific.
 * </p>
 */
public class ODataRootLocator {

  @Context
  private ContextResolver<ODataProcessor> resolver;
  @Context
  private HttpHeaders httpHeaders;
  @Context
  private UriInfo uriInfo;
  @Context
  private Request request;

  /**
   * Default root behavior which will delegate all paths to a ODataLocator.
   * @param odataPathSegments all segments have to be OData
   * @return a locator handling OData protocol
   * @throws ODataError 
   */
  @Path("/{odataPathSegments: .*}")
  public ODataLocator getSubLocator(@PathParam("odataPathSegments") List<PathSegment> odataPathSegments) throws ODataError {
    ODataLocator odataLocator = RuntimeDelegate.getInstance().createODataLocator();

    odataLocator.setProcessor(this.resolver.getContext(ODataProcessor.class));
    odataLocator.setPathSegments(odataPathSegments);
    odataLocator.setHttpHeaders(this.httpHeaders);
    odataLocator.setUriInfo(this.uriInfo);

    odataLocator.beforRequest();

    return odataLocator;

  }
}