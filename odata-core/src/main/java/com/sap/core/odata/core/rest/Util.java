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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.processor.ODataResponse;

public class Util {
  public static Response convertResponse(final ODataResponse odataResponse, final HttpStatusCodes s, final String version, final String location) {
    ResponseBuilder responseBuilder = Response.noContent().status(s.getStatusCode()).entity(odataResponse.getEntity());

    for (final String name : odataResponse.getHeaderNames()) {
      responseBuilder = responseBuilder.header(name, odataResponse.getHeader(name));
    }

    if (!odataResponse.containsHeader(ODataHttpHeaders.DATASERVICEVERSION)) {
      responseBuilder = responseBuilder.header(ODataHttpHeaders.DATASERVICEVERSION, version);
    }

    if (!odataResponse.containsHeader(HttpHeaders.LOCATION) && location != null) {
      responseBuilder = responseBuilder.header(HttpHeaders.LOCATION, location);
    }

    final String eTag = odataResponse.getETag();
    if (eTag != null) {
      responseBuilder.header(HttpHeaders.ETAG, eTag);
    }

    return responseBuilder.build();
  }

}
