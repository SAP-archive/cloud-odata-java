package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing a link in JSON.
 * @author SAP AG
 */
public class JsonLinkEntityProducer {

  private final EntityProviderProperties properties;

  public JsonLinkEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(Writer writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    final String uri = properties.getServiceRoot().toASCIIString()
        + AtomEntryEntityProducer.createSelfLink(entityInfo, data, null);
    try {
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      appendUri(writer, uri);
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendUri(Writer writer, final String uri) throws IOException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    jsonStreamWriter.beginObject();
    jsonStreamWriter.namedStringValue(FormatJson.URI, uri);
    jsonStreamWriter.endObject();
  }
}
