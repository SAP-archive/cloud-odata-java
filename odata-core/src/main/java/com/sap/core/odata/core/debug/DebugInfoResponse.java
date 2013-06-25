package com.sap.core.odata.core.debug;

import java.io.IOException;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * @author SAP AG
 */
public class DebugInfoResponse implements DebugInfo {

  private final HttpStatusCodes status;
  private final Map<String, String> headers;

  public DebugInfoResponse(final HttpStatusCodes status, final Map<String, String> headers) {
    this.status = status;
    this.headers = headers;
  }

  @Override
  public String getName() {
    return "Response";
  }

  @Override
  public void appendJson(final JsonStreamWriter jsonStreamWriter) throws IOException {
    jsonStreamWriter.beginObject();
    jsonStreamWriter.name("status");
    jsonStreamWriter.beginObject();
    jsonStreamWriter.name("code");
    jsonStreamWriter.unquotedValue(Integer.toString(status.getStatusCode()));
    jsonStreamWriter.separator();
    jsonStreamWriter.namedStringValueRaw("info", status.getInfo());
    jsonStreamWriter.endObject();
    jsonStreamWriter.separator();

    jsonStreamWriter.name("headers");
    jsonStreamWriter.beginObject();
    boolean first = true;
    for (final String name : headers.keySet()) {
      if (!first)
        jsonStreamWriter.separator();
      first = false;
      jsonStreamWriter.namedStringValue(name, headers.get(name));
    }
    jsonStreamWriter.endObject();
    jsonStreamWriter.endObject();
  }
}
