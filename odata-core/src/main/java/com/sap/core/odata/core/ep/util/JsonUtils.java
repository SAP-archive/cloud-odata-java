package com.sap.core.odata.core.ep.util;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.ep.EntityProviderException;

public class JsonUtils {

  public static int startJson(final JsonReader reader) throws EntityProviderException {
    //The enclosing "d" and "results" are optional - so we cannot check for the presence
    //but we have to read over them in case they are present.
    JsonToken token;
    try {
      token = reader.peek();
      int openJsonObjects = 0;
      if (JsonToken.BEGIN_OBJECT == token) {
        reader.beginObject();
        openJsonObjects++;
        token = reader.peek();
        if (JsonToken.NAME == token) {
          String name = reader.nextName();
          if (!("d".equals(name) ^ "results".equals(name))) {
            //TODO I18N
            throw new EntityProviderException(EntityProviderException.COMMON, name + " not expected, only d or results");
          }
        }

        token = reader.peek();
        if (JsonToken.BEGIN_OBJECT == token) {
          reader.beginObject();
          openJsonObjects++;
        } else if (JsonToken.BEGIN_ARRAY == token) {
          //TODO I18N
          throw new EntityProviderException(EntityProviderException.COMMON, "Array not expected");
        }
      }

      return openJsonObjects;
    } catch (IOException e) {
      //TODO I18N
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
  }

  public static boolean endJson(final JsonReader reader, final int openJsonObjects) throws IOException, EntityProviderException {

    for (int closedJsonObjects = 0; closedJsonObjects < openJsonObjects; closedJsonObjects++) {
      reader.endObject();
    }
    return reader.peek() == JsonToken.END_DOCUMENT;
  }
}
