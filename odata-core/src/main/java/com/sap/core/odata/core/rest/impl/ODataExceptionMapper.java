package com.sap.core.odata.core.rest.impl;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ODataExceptionMapper implements ExceptionMapper<RuntimeException> {

  private final static Logger log = LoggerFactory.getLogger(ODataExceptionMapper.class);

  @Context
  protected UriInfo uriInfo;
  @Context
  protected HttpHeaders httpHeaders;

  @Override
  public Response toResponse(RuntimeException exception) {

    String entity;
    Status status;

    try {
      throw exception;
    } catch (RuntimeException e) {
      ODataExceptionMapper.log.error(e.getMessage(), e);
      entity = exception.getClass().getCanonicalName() + " - " + exception.getMessage();
      status = Status.INTERNAL_SERVER_ERROR;
    }

    return Response.status(status).entity(entity).build();
  }

}
