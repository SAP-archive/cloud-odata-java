package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySetInfo;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Writes the  OData service document in JSON.
 * @author SAP AG
 */
public class JsonServiceDocumentProducer {

  public static void writeServiceDocument(final Writer writer, final Edm edm) throws EntityProviderException {
    EdmServiceMetadata serviceMetadata = edm.getServiceMetadata();

    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    try {
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.ENTITY_SETS);
      jsonStreamWriter.beginArray();

      boolean first = true;
      for (EdmEntitySetInfo info : serviceMetadata.getEntitySetInfos()) {
        if (first) {
          first = false;
        } else {
          jsonStreamWriter.separator();
        }
        jsonStreamWriter.stringValue(info.getEntitySetUri().toASCIIString());
      }

      jsonStreamWriter.endArray();
      jsonStreamWriter.endObject();
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final ODataException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

  }
}