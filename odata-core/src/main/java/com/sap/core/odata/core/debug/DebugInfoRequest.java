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
