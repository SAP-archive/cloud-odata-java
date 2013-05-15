package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing a link collection in JSON.
 * @author SAP AG
 */
public class JsonLinksEntityProducer {

  private final EntityProviderWriteProperties properties;

  public JsonLinksEntityProducer(final EntityProviderWriteProperties properties) throws EntityProviderException {
    this.properties = properties == null ? EntityProviderWriteProperties.serviceRoot(null).build() : properties;
  }

  public void append(final Writer writer, final EntityInfoAggregator entityInfo, final List<Map<String, Object>> data) throws EntityProviderException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    try {
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);

      if (properties.getInlineCountType() == InlineCount.ALLPAGES) {
        final int inlineCount = properties.getInlineCount() == null ? 0 : properties.getInlineCount();
        jsonStreamWriter.beginObject();
        jsonStreamWriter.namedStringValueRaw(FormatJson.COUNT, String.valueOf(inlineCount));
        jsonStreamWriter.separator();
        jsonStreamWriter.name(FormatJson.RESULTS);
      }

      jsonStreamWriter.beginArray();
      final String serviceRoot = properties.getServiceRoot().toASCIIString();
      boolean first = true;
      for (final Map<String, Object> entryData : data) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        JsonLinkEntityProducer.appendUri(jsonStreamWriter,
            (serviceRoot == null ? "" : serviceRoot) + AtomEntryEntityProducer.createSelfLink(entityInfo, entryData, null));
      }
      jsonStreamWriter.endArray();

      if (properties.getInlineCountType() == InlineCount.ALLPAGES) {
        jsonStreamWriter.endObject();
      }

      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
