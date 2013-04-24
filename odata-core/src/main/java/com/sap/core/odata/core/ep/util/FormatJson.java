package com.sap.core.odata.core.ep.util;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * String constants for formatting and parsing of JSON.
 * @author SAP AG
 */
public class FormatJson {

  public static final String D = "d";
  public static final String RESULTS = "results";
  public static final String COUNT = "__count";
  public static final String METADATA = "__metadata";
  public static final String DEFERRED = "__deferred";
  public static final String ID = "id";
  public static final String CONTENT_TYPE = "content_type";
  public static final String MEDIA_SRC = "media_src";
  public static final String MEDIA_ETAG = "media_etag";
  public static final String EDIT_MEDIA = "edit_media";
  public static final String URI = "uri";
  public static final String NULL = "null";
  public static final String TRUE = "true";
  public static final String FALSE = "false";
  public static final String TYPE = "type";
  public static final String ETAG = "etag";
  public static final String ENTITY_SETS = "EntitySets";
  public static final String NEXT = "__next";
  public static final String ERROR = "error";
  public static final String CODE = "code";
  public static final String MESSAGE = "message";
  public static final String LANG = "lang";
  public static final String VALUE = "value";
  public static final String INNER_ERROR = "innererror";

  public static int startJson(JsonReader reader) throws EntityProviderException {
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

  public static boolean endJson(JsonReader reader, int openJsonObjects) throws IOException, EntityProviderException {

    for (int closedJsonObjects = 0; closedJsonObjects < openJsonObjects; closedJsonObjects++) {
      reader.endObject();
    }
    return reader.peek() == JsonToken.END_DOCUMENT;
  }
}
