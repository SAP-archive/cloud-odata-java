/**
 * (c) 2013 by SAP AG
 */
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
