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

  public void append(final Writer writer, final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    try {
      JsonStreamWriter.beginObject(writer);
      JsonStreamWriter.name(writer, FormatJson.D);
      appendUri(writer, properties, entityInfo, data);
      JsonStreamWriter.endObject(writer);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  protected static void appendUri(final Writer writer, final EntityProviderProperties properties, final EntityInfoAggregator entityInfo, final Map<String, Object> data) throws EntityProviderException {
    final String uri = properties.getSelfLink() == null ?
        properties.getServiceRoot().toASCIIString() + AtomEntryEntityProducer.createSelfLink(entityInfo, data, null) :
        properties.getSelfLink().toASCIIString();
    try {
      JsonStreamWriter.beginObject(writer);
      JsonStreamWriter.namedStringValue(writer, FormatJson.URI, uri);
      JsonStreamWriter.endObject(writer);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
