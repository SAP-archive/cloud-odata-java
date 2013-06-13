package com.sap.core.odata.core.rest;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;
import com.sap.core.odata.api.uri.PathInfo;

public class ODataErrorHandlerCallbackImpl implements ODataErrorCallback {

  @Override
  public ODataResponse handleError(final ODataErrorContext context) {
    ODataResponseBuilder responseBuilder = ODataResponse.entity("bla").status(HttpStatusCodes.BAD_REQUEST).contentHeader("text/html");

    if (context.getRequestUri() != null) {
      responseBuilder.header("RequestUri", context.getRequestUri().toASCIIString());
      PathInfo pathInfo = context.getPathInfo();
      if(pathInfo == null) {
        responseBuilder.header("PathInfo", "NULL");
      } else {
        responseBuilder.header("PathInfo", "TRUE");
        responseBuilder.header("PathInfo.oDataSegments", pathInfo.getODataSegments().toString());
        responseBuilder.header("PathInfo.precedingSegments", pathInfo.getPrecedingSegments().toString());
        responseBuilder.header("PathInfo.requestUri", pathInfo.getRequestUri().toString());
        responseBuilder.header("PathInfo.serviceRoot", pathInfo.getServiceRoot().toString());
      }
    }

    Map<String, List<String>> requestHeaders = context.getRequestHeaders();
    if (requestHeaders != null && requestHeaders.entrySet() != null) {
      Set<Entry<String, List<String>>> entries = requestHeaders.entrySet();
      for (Entry<String, List<String>> entry : entries) {
        responseBuilder.header(entry.getKey(), entry.getValue().toString());
      }
    }

    return responseBuilder.build();
  }

}
