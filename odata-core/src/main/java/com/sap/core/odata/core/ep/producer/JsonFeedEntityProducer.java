/**
 * (c) 2013 by SAP AG
 */
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
 * Producer for writing an entity collection (a feed) in JSON.
 * @author SAP AG
 */
public class JsonFeedEntityProducer {

  private final EntityProviderWriteProperties properties;

  public JsonFeedEntityProducer(final EntityProviderWriteProperties properties) throws EntityProviderException {
    this.properties = properties;
  }

  public void append(final Writer writer, final EntityInfoAggregator entityInfo, final List<Map<String, Object>> data, final boolean isRootElement) throws EntityProviderException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    try {
      jsonStreamWriter.beginObject();

      if (isRootElement) {
        jsonStreamWriter.name(FormatJson.D);
        jsonStreamWriter.beginObject();
      }

      if (properties.getInlineCountType() == InlineCount.ALLPAGES) {
        jsonStreamWriter.namedStringValueRaw(FormatJson.COUNT, String.valueOf(properties.getInlineCount()));
        jsonStreamWriter.separator();
      }

      jsonStreamWriter.name(FormatJson.RESULTS);
      jsonStreamWriter.beginArray();
      JsonEntryEntityProducer entryProducer = new JsonEntryEntityProducer(properties);
      boolean first = true;
      for (final Map<String, Object> entryData : data) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        entryProducer.append(writer, entityInfo, entryData, false);
      }
      jsonStreamWriter.endArray();

      // Write "next" link.
      // To be compatible with other implementations out there, the link is
      // written directly after "__next" and not as "{"uri":"next link"}",
      // deviating from the OData 2.0 specification.
      if (properties.getNextLink() != null) {
        jsonStreamWriter.separator();
        jsonStreamWriter.namedStringValue(FormatJson.NEXT, properties.getNextLink());
      }

      if (isRootElement) {
        jsonStreamWriter.endObject();
      }

      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
