package com.sap.core.odata.core.rest;

import java.net.URI;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

import com.sap.core.odata.core.ODataLocator;

public class ODataRedirectLocator implements ODataLocator {

  @GET
  public Response redirectGet() {
    return redirect();
  }

  @PUT
  public Response redirectPut() {
    return redirect();
  }

  @POST
  public Response redirectPost() {
    return redirect();
  }

  @DELETE
  public Response redirectDelete() {
    return redirect();
  }

  @OPTIONS
  public Response redirectOptions() {
    return redirect();
  }

  @HEAD
  public Response redirectHead() {
    return redirect();
  }

  @PATCH
  public Response redirectPatch() {
    return redirect();
  }

  @MERGE
  public Response redirectMerge() {
    return redirect();
  }

  private Response redirect() {
    return Response.temporaryRedirect(URI.create("/")).build();
  }

}
