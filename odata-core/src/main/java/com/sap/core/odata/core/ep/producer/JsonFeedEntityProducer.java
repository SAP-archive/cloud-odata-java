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
 * Producer for writing an entity collection (a feed) in JSON.
 * @author SAP AG
 */
public class JsonFeedEntityProducer {

  private final EntityProviderProperties properties;

  public JsonFeedEntityProducer(final EntityProviderProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(Writer writer, final EntityInfoAggregator entityInfo, final List<Map<String, Object>> data) throws EntityProviderException {
    try {
      JsonStreamWriter.beginObject(writer);
      JsonStreamWriter.name(writer, FormatJson.D);
      JsonStreamWriter.beginObject(writer);

      if (properties.getInlineCountType() == InlineCount.ALLPAGES) {
        JsonStreamWriter.namedStringValueRaw(writer, FormatJson.COUNT, String.valueOf(properties.getInlineCount()));
        JsonStreamWriter.separator(writer);
      }

      JsonStreamWriter.name(writer, FormatJson.RESULTS);
      JsonStreamWriter.beginArray(writer);
      boolean first = true;
      for (final Map<String, Object> entryData : data) {
        if (first)
          first = false;
        else
          JsonStreamWriter.separator(writer);
        new JsonEntryEntityProducer(properties).append(writer, entityInfo, entryData, false);
      }
      JsonStreamWriter.endArray(writer);
      JsonStreamWriter.endObject(writer);
      JsonStreamWriter.endObject(writer);
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
