package com.sap.core.odata.core.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.core.rest.impl.ODataContextImpl;
import com.sap.core.odata.core.rest.impl.ODataLocatorImpl;

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

  private static final Logger log = LoggerFactory.getLogger(ODataRootLocator.class);

  @Context
  private ContextResolver<ODataProducer> resolver;
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
   */
  @Path("/{odataPathSegments: .*}")
  public ODataLocator getSubLocator(@PathParam("odataPathSegments") List<PathSegment> odataPathSegments) {
    ODataRootLocator.log.debug("+++ ODataSubResourceLocator.getODataResource()");

    ODataProducer producer = this.resolver.getContext(ODataProducer.class);

    ODataLocatorImpl odataLocator = new ODataLocatorImpl();
    ODataContextImpl context = new ODataContextImpl();

    context.setHttpHeaders(httpHeaders);
    context.setRequest(request);
    context.setUriInfo(uriInfo);
    context.setPathSegments(odataPathSegments);

    odataLocator.setProducer(producer);
    odataLocator.setContext(context);
    
    odataLocator.beforRequest();
    
    return odataLocator;
  }
  
}
