package com.sap.core.odata.core.debug;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;
import com.sap.core.odata.core.exception.MessageService;
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * Wraps an OData response into an OData response containing additional
 * information useful for support purposes.
 * @author SAP AG
 */
public class ODataDebugResponseWrapper {

  public static final String ODATA_DEBUG_QUERY_PARAMETER = "odata-debug";
  public static final String ODATA_DEBUG_JSON = "json";

  private final ODataContext context;
  private ODataResponse response;
  private UriInfo uriInfo;
  private Exception exception;
  private final boolean isJson;

  public ODataDebugResponseWrapper(final ODataContext context, final ODataResponse response, final UriInfo uriInfo, final Exception exception, final String debugValue) {
    this.context = context;
    this.response = response;
    this.uriInfo = uriInfo;
    this.exception = exception;
    isJson = ODATA_DEBUG_JSON.equals(debugValue);
  }

  public ODataResponse wrapResponse() {
    try {
      return ODataResponse.status(HttpStatusCodes.OK)
          .entity(isJson ? wrapInJson(createParts()) : null)
          .contentHeader(isJson ? HttpContentType.APPLICATION_JSON : null)
          .build();
    } catch (final ODataException e) {
      throw new ODataRuntimeException("Should not happen", e);
    } catch (final IOException e) {
      throw new ODataRuntimeException("Should not happen", e);
    }
  }

  private List<DebugInfo> createParts() throws ODataException {
    List<DebugInfo> parts = new ArrayList<DebugInfo>();

    // body
    parts.add(new DebugInfoBody(response));

    // request
    parts.add(new DebugInfoRequest(context.getHttpMethod(),
        context.getPathInfo().getRequestUri(),
        context.getRequestHeaders()));

    // response
    Map<String, String> responseHeaders = new HashMap<String, String>();
    for (final String name : response.getHeaderNames()) {
      responseHeaders.put(name, response.getHeader(name));
    }
    parts.add(new DebugInfoResponse(response.getStatus(), responseHeaders));

    // URI
    parts.add(new DebugInfoUri(uriInfo, exception));

    // runtime measurements
    if (context.getRuntimeMeasurements() != null) {
      parts.add(new DebugInfoRuntime(context.getRuntimeMeasurements()));
    }

    // exceptions
    if (exception != null) {
      final Locale locale = MessageService.getSupportedLocale(context.getAcceptableLanguages(), Locale.ENGLISH);
      parts.add(new DebugInfoException(exception, locale));
    }

    return parts;
  }

  private String wrapInJson(final List<DebugInfo> parts) throws IOException {
    StringWriter writer = new StringWriter();
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginObject();
    boolean first = true;
    for (final DebugInfo part : parts) {
      if (!first) {
        jsonStreamWriter.separator();
      }
      first = false;
      jsonStreamWriter.name(part.getName().toLowerCase(Locale.ROOT));
      part.appendJson(jsonStreamWriter);
    }
    jsonStreamWriter.endObject();
    return writer.toString();
  }
}
