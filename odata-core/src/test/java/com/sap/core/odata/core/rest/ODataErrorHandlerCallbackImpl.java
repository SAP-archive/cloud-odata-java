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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataErrorCallback;
import com.sap.core.odata.api.processor.ODataErrorContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataResponse.ODataResponseBuilder;

public class ODataErrorHandlerCallbackImpl implements ODataErrorCallback {

  @Override
  public ODataResponse handleError(final ODataErrorContext context) {
    ODataResponseBuilder responseBuilder = ODataResponse.entity("bla").status(HttpStatusCodes.BAD_REQUEST).contentHeader("text/html");

    if (context.getRequestUri() != null) {
      responseBuilder.header("RequestUri", context.getRequestUri().toASCIIString());
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
