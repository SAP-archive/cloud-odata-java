package com.sap.core.odata.core.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.core.rest.impl.MERGE;
import com.sap.core.odata.core.rest.impl.PATCH;

public interface ODataSubLocator {

  public abstract ODataProducer getProducer();

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handleGet();

  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      );

  @PUT
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handlePut();

  @PATCH
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handlePatch();

  @MERGE
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handleMerge();

  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handleDelete();

  public abstract void setContext(ODataContext context);

  public abstract void setProducer(ODataProducer producer);
}