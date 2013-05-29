package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.util.FormatJson;

/**
 * @author SAP AG
 */
public class JsonLinkConsumer {

  /**
   * Reads single link with format <code>{"d":{"uri":"http://somelink"}}</code>
   * or <code>{"uri":"http://somelink"}</code>.
   * @param reader
   * @param entitySet
   * @return link as string object
   * @throws EntityProviderException
   */
  public String readLink(JsonReader reader, final EdmEntitySet entitySet) throws EntityProviderException {
    // TODO: message texts
    try {
      String result;
      reader.beginObject();
      String nextName = reader.nextName();
      final boolean wrapped = FormatJson.D.equals(nextName);
      if (wrapped) {
        reader.beginObject();
        nextName = reader.nextName();
      }
      if (!FormatJson.URI.equals(nextName))
        throw new EntityProviderException(EntityProviderException.COMMON);
      final JsonToken tokenType = reader.peek();
      if (tokenType == JsonToken.STRING)
        result = reader.nextString();
      else
        throw new EntityProviderException(EntityProviderException.COMMON);
      reader.endObject();
      if (wrapped)
        reader.endObject();

      reader.peek(); // to assert end of document

      return result;
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    } catch (final IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
  }
}
