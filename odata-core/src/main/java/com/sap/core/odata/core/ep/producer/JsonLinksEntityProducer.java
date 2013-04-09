package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Producer for writing a link collection in JSON.
 * @author SAP AG
 */
public class JsonLinksEntityProducer {

  private final EntityProviderProperties properties;

  public JsonLinksEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(Writer writer, final EntityInfoAggregator entityInfo, final List<Map<String, Object>> data) throws EntityProviderException {
    try {
      JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);

      if (properties.getInlineCountType() == InlineCount.ALLPAGES) {
        jsonStreamWriter.beginObject();
        jsonStreamWriter.namedStringValueRaw(FormatJson.COUNT, String.valueOf(properties.getInlineCount()));
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
        JsonLinkEntityProducer.appendUri(writer, serviceRoot + AtomEntryEntityProducer.createSelfLink(entityInfo, entryData, null));
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
