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
package com.sap.core.odata.core.debug;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class DebugInfoRequest implements DebugInfo {

  private final String method;
  private final URI uri;
  private final Map<String, List<String>> headers;

  public DebugInfoRequest(final String method, final URI uri, final Map<String, List<String>> headers) {
    this.method = method;
    this.uri = uri;
    this.headers = headers;
  }

  @Override
  public String getName() {
    return "Request";
  }

  @Override
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject()
        .namedStringValueRaw("method", method).separator()
        .namedStringValue("uri", uri.toString());

    if (!headers.isEmpty()) {
      jsonStreamWriter.separator()
          .name("headers")
          .beginObject();
      boolean first = true;
      for (final String name : headers.keySet()) {
        for (final String value : headers.get(name)) {
          if (!first) {
            jsonStreamWriter.separator();
          }
          first = false;
          jsonStreamWriter.namedStringValue(name, value);
        }
      }
      jsonStreamWriter.endObject();
    }

    jsonStreamWriter.endObject();
  }
}
