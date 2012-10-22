package com.sap.core.odata.core.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.core.rest.impl.MERGE;
import com.sap.core.odata.core.rest.impl.PATCH;

/**
 * JAX-RS locator handling OData protocol
 */
public interface ODataLocator {

  /**
   * @return a concrete OData producer
   */
  public abstract ODataProcessor getProcessor() throws ODataError;

  /**
   * initialze OData locator before request handling
   */
  public abstract void beforRequest() throws ODataError;

  /**
   * handle OData request
   * @return OData response
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handleGet() throws ODataError;

  /**
   * handle OData request
   * @param xmethod - method tunneling support by X-HTTP-Method header
   * @return OData response
   */
  @POST
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handlePost(
      @HeaderParam("X-HTTP-Method") String xmethod
      ) throws ODataError;

  /**
   * handle OData request
   * @return OData response
   */
  @PUT
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handlePut() throws ODataError;

  /**
   * handle OData request
   * @return OData response
   */
  @PATCH
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handlePatch() throws ODataError;

  /**
   * handle OData request
   * @return OData response
   */
  @MERGE
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handleMerge() throws ODataError;

  /**
   * handle OData request
   * @return OData response
   */
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public abstract Response handleDelete() throws ODataError;

}