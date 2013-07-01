package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing a link in JSON.
 * @author SAP AG
 */
public class JsonLinkEntityProducer {

  private final EntityProviderWriteProperties properties;

  public JsonLinkEntityProducer(final EntityProviderWriteProperties properties) throws EntityProviderException {
    this.properties = properties == null ? EntityProviderWriteProperties.serviceRoot(null).build() : properties;
  }

  public void append(final Writer writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    final String uri = (properties.getServiceRoot() == null ? "" : properties.getServiceRoot().toASCIIString())
        + AtomEntryEntityProducer.createSelfLink(entityInfo, data, null);
    try {
      jsonStreamWriter.beginObject()
          .name(FormatJson.D);
      appendUri(jsonStreamWriter, uri);
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendUri(final JsonStreamWriter jsonStreamWriter, final String uri) throws IOException {
    jsonStreamWriter.beginObject()
        .namedStringValue(FormatJson.URI, uri)
        .endObject();
  }
}
