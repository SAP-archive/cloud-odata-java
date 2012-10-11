package com.sap.core.odata.rest;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.producer.Entity;
import com.sap.core.odata.producer.ODataProducer;
import com.sap.core.odata.rest.impl.MERGE;
import com.sap.core.odata.rest.impl.PATCH;

public final class ODataSubResource {

  private static final Logger log = LoggerFactory.getLogger(ODataSubResource.class);

  private HttpHeaders httpHeaders;
  private UriInfo uriInfo;
  private Request request;

  private ODataProducer producer;

  private List<PathSegment> odataPathSegments;

  public void setHttpHeaders(HttpHeaders httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

  public void setUriInfo(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  public void setRequest(Request request) {
    this.request = request;
  }

  public void setProducer(ODataProducer producer) {
    this.producer = producer;
  }

  public ODataProducer getProducer() {
    return this.producer;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response handleGet() {
    ODataSubResource.log.debug("+++ ODataSubResource:handleGet()");
    this.logContext();

    if (this.producer instanceof Entity) {
      Entity entityProducer = (Entity) this.producer;
      entityProducer.read();
    }
    
    return Response.ok().entity("GET: status 200 ok").build();
  }

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      ) {

    ODataSubResource.log.debug("+++ ODataSubResource:handlePost()");
    Response response;

    /* tunneling support */
    if (xmethod == null) {
      this.logContext();
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

  @PUT
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePut() {
    ODataSubResource.log.debug("+++ ODataSubResource:handlePut()");
    this.logContext();

    return Response.ok().entity("PUT: status 200 ok").build();
  }

  @PATCH
  @Produces(MediaType.TEXT_PLAIN)
  public Response handlePatch() {
    ODataSubResource.log.debug("+++ ODataSubResource:handlePatch()");
    this.logContext();

    return Response.ok().entity("PATCH: status 200 ok").build();
  }

  @MERGE
  @Produces(MediaType.TEXT_PLAIN)
  public Response handleMerge() {
    ODataSubResource.log.debug("+++ ODataSubResource:handleMerge()");
    this.logContext();

    return Response.ok().entity("MERGE: status 200 ok").build();
  }

  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public Response handleDelete() {
    ODataSubResource.log.debug("+++ ODataSubResource:handleDelete()");
    this.logContext();

    return Response.ok().entity("DELETE: status 200 ok").build();
  }

  private void logContext() {
    ODataSubResource.log.debug("==================================");
    ODataSubResource.log.debug("producer:     " + this.producer);
    ODataSubResource.log.debug("odataPath:    " + this.odataPathSegments);
    ODataSubResource.log.debug("httpHeaders:  " + (this.httpHeaders != null ? this.httpHeaders.getRequestHeaders() : null));
    ODataSubResource.log.debug("request:      " + (this.request != null ? this.request.getMethod() : null));
    ODataSubResource.log.debug("----------------------------------");
    ODataSubResource.log.debug("uriInfo:      " + (this.uriInfo != null ? this.uriInfo.getPath() : null));
    ODataSubResource.log.debug("baseUri:      " + (this.uriInfo != null ? this.uriInfo.getBaseUri() : null));
    ODataSubResource.log.debug("queryParam:   " + (this.uriInfo != null ? this.uriInfo.getQueryParameters() : null));
    ODataSubResource.log.debug("pathParam:    " + (this.uriInfo != null ? this.uriInfo.getPathParameters() : null));
    ODataSubResource.log.debug("pathSegments: " + (this.uriInfo != null ? this.uriInfo.getPathSegments() : null));
    ODataSubResource.log.debug("----------------------------------");
  }

  public void setODataPathSegments(List<PathSegment> odataPathSegments) {
    this.odataPathSegments = odataPathSegments;
  }
}
