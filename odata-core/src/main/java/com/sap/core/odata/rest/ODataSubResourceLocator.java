package com.sap.core.odata.rest;

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

import com.sap.core.odata.producer.ODataProducer;

public class ODataSubResourceLocator {

  private static final Logger log = LoggerFactory.getLogger(ODataSubResourceLocator.class);

  @Context
  private ContextResolver<ODataProducer> resolver;
  @Context
  private HttpHeaders httpHeaders;
  @Context
  private UriInfo uriInfo;
  @Context
  private Request request;
  
  @Path("/{odataPathSegments: .*}")
  public ODataSubResource getODataResource(@PathParam("odataPathSegments") List<PathSegment> odataPathSegments) {
    ODataSubResourceLocator.log.debug("+++ ODataSubResourceLocator.getODataResource()");

    ODataProducer producer = this.resolver.getContext(ODataProducer.class);
    ODataSubResource resource = new ODataSubResource();

    resource.setODataPathSegments(odataPathSegments);
    resource.setHttpHeaders(this.httpHeaders);
    resource.setProducer(producer);
    resource.setRequest(request);
    resource.setUriInfo(uriInfo);
    
    return resource;
  }
  
}
