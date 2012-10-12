package com.sap.core.odata.core.rest.impl;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.core.producer.Entity;
import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.core.rest.ODataContext;
import com.sap.core.odata.core.rest.ODataSubLocator;

public final class ODataSubLocatorImpl implements ODataSubLocator {

  private static final Logger log = LoggerFactory.getLogger(ODataSubLocatorImpl.class);

  private ODataContext context;

  private ODataProducer producer;

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#getProducer()
   */
  @Override
  public ODataProducer getProducer() {
    return this.producer;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#handleGet()
   */
  @Override
  public Response handleGet() {
    ODataSubLocatorImpl.log.debug("+++ ODataSubResource:handleGet()");
    this.log();

    if (this.producer instanceof Entity) {
      Entity entityProducer = (Entity) this.producer;
      entityProducer.read();
    }

    return Response.ok().entity("GET: status 200 ok").build();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#handlePost(java.lang.String)
   */
  @Override
  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      ) {

    ODataSubLocatorImpl.log.debug("+++ ODataSubResource:handlePost()");
    Response response;

    /* tunneling support */
    if (xmethod == null) {
      this.log();
      response = Response.ok().entity("POST: status 200 ok").build();
    } else if ("MERGE".equals(xmethod)) {
      response = this.handleMerge();
    } else if ("PATCH".equals(xmethod)) {
      response = this.handlePatch();
    } else {
      response = Response.status(405).build(); // method not allowed!
    }

    return response;
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#handlePut()
   */
  @Override
  public Response handlePut() {
    ODataSubLocatorImpl.log.debug("+++ ODataSubResource:handlePut()");
    this.log();

    return Response.ok().entity("PUT: status 200 ok").build();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#handlePatch()
   */
  @Override
  public Response handlePatch() {
    ODataSubLocatorImpl.log.debug("+++ ODataSubResource:handlePatch()");
    this.log();

    return Response.ok().entity("PATCH: status 200 ok").build();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#handleMerge()
   */
  @Override
  public Response handleMerge() {
    ODataSubLocatorImpl.log.debug("+++ ODataSubResource:handleMerge()");
    this.log();

    return Response.ok().entity("MERGE: status 200 ok").build();
  }

  /* (non-Javadoc)
   * @see com.sap.core.odata.rest.impl.ODataSubResource#handleDelete()
   */
  @Override
  public Response handleDelete() {
    ODataSubLocatorImpl.log.debug("+++ ODataSubResource:handleDelete()");
    this.log();

    return Response.ok().entity("DELETE: status 200 ok").build();
  }

  @Override
  public void setContext(ODataContext context) {
    this.context = context;
  }

  @Override
  public void setProducer(ODataProducer producer) {
    this.producer = producer;
  }

  private void log() {
    ODataSubLocatorImpl.log.debug("--odata http context----------------");
    ODataSubLocatorImpl.log.debug("odataPath:      " + this.context.getPathSegments());
    ODataSubLocatorImpl.log.debug("httpHeaders:    " + this.context.getHttpHeaders().getRequestHeaders());
    ODataSubLocatorImpl.log.debug("request method: " + this.context.getRequest().getMethod());
    ODataSubLocatorImpl.log.debug("----------------------------------");
    ODataSubLocatorImpl.log.debug("uriInfo:        " + this.context.getUriInfo().getPath());
    ODataSubLocatorImpl.log.debug("baseUri:        " + this.context.getUriInfo().getBaseUri());
    ODataSubLocatorImpl.log.debug("queryParam:     " + this.context.getUriInfo().getQueryParameters());
    ODataSubLocatorImpl.log.debug("pathParam:      " + this.context.getUriInfo().getPathParameters());
    ODataSubLocatorImpl.log.debug("pathSegments:   " + this.context.getUriInfo().getPathSegments());
    ODataSubLocatorImpl.log.debug("----------------------------------");
  }

}
