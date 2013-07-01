package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.aggregator.EntityPropertyInfo;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Provider for writing a collection of simple-type or complex-type instances in JSON.
 * @author SAP AG
 */
public class JsonCollectionEntityProducer {

  public void append(final Writer writer, final EntityPropertyInfo propertyInfo, final List<?> data) throws EntityProviderException {
    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);

    try {
      jsonStreamWriter.beginObject()
          .name(FormatJson.D)
          .beginObject();

      jsonStreamWriter.name(FormatJson.METADATA)
          .beginObject()
          .namedStringValueRaw(FormatJson.TYPE,
              "Collection(" + propertyInfo.getType().getNamespace() + Edm.DELIMITER + propertyInfo.getType().getName() + ")")
          .endObject()
          .separator();

      jsonStreamWriter.name(FormatJson.RESULTS)
          .beginArray();
      boolean first = true;
      for (final Object item : data) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        JsonPropertyEntityProducer.appendPropertyValue(jsonStreamWriter, propertyInfo, item);
      }
      jsonStreamWriter.endArray();

      jsonStreamWriter.endObject()
          .endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }
}
