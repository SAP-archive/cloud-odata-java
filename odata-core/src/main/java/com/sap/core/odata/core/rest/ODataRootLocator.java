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
import com.sap.core.odata.core.rest.impl.ODataSubLocatorImpl;

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
  
  @Path("/{odataPathSegments: .*}")
  public ODataSubLocator getSubLocator(@PathParam("odataPathSegments") List<PathSegment> odataPathSegments) {
    ODataRootLocator.log.debug("+++ ODataSubResourceLocator.getODataResource()");

    ODataProducer producer = this.resolver.getContext(ODataProducer.class);

    ODataSubLocatorImpl subLocator = new ODataSubLocatorImpl();
    ODataContextImpl context = new ODataContextImpl();

    context.setHttpHeaders(httpHeaders);
    context.setRequest(request);
    context.setUriInfo(uriInfo);
    context.setPathSegments(odataPathSegments);

    subLocator.setProducer(producer);
    subLocator.setContext(context);
    
    return subLocator;
  }
  
}
