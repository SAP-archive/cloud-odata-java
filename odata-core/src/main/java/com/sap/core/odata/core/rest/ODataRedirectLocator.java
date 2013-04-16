/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.rest;

import java.net.URI;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

/**
 * @author SAP AG
 */
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
