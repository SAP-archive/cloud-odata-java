package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.core.ep.feed.FeedMetadataImpl;
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
  public String readLink(final JsonReader reader, final EdmEntitySet entitySet) throws EntityProviderException {
    try {
      String result;
      reader.beginObject();
      String nextName = reader.nextName();
      final boolean wrapped = FormatJson.D.equals(nextName);
      if (wrapped) {
        reader.beginObject();
        nextName = reader.nextName();
      }
      if (FormatJson.URI.equals(nextName) && reader.peek() == JsonToken.STRING) {
        result = reader.nextString();
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_CONTENT.addContent(FormatJson.D + " or " + FormatJson.URI).addContent(nextName));
      }
      reader.endObject();
      if (wrapped) {
        reader.endObject();
      }

      reader.peek(); // to assert end of structure or document

      return result;
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    } catch (final IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
  }

  /**
   * Reads a collection of links, optionally wrapped in a "d" object,
   * and optionally wrapped in an "results" object, where an additional "__count"
   * object could appear on the same level as the "results".
   * @param reader
   * @param entitySet
   * @return links as List of Strings
   * @throws EntityProviderException
   */
  public List<String> readLinks(final JsonReader reader, final EdmEntitySet entitySet) throws EntityProviderException {
    List<String> links = null;
    int openedObjects = 0;

    try {
      String nextName;
      if (reader.peek() == JsonToken.BEGIN_ARRAY) {
        nextName = FormatJson.RESULTS;
      } else {
        reader.beginObject();
        openedObjects++;
        nextName = reader.nextName();
      }
      if (FormatJson.D.equals(nextName)) {
        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
          nextName = FormatJson.RESULTS;
        } else {
          reader.beginObject();
          openedObjects++;
          nextName = reader.nextName();
        }
      }
      FeedMetadataImpl feedMetadata = new FeedMetadataImpl();
      if (FormatJson.COUNT.equals(nextName)) {
        JsonFeedConsumer.readInlineCount(reader, feedMetadata);
        nextName = reader.nextName();
      }
      if (FormatJson.RESULTS.equals(nextName)) {
        links = readLinksArray(reader);
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_CONTENT.addContent(FormatJson.RESULTS).addContent(nextName));
      }
      if (reader.hasNext() && reader.peek() == JsonToken.NAME) {
        if (FormatJson.COUNT.equals(reader.nextName())) {
          JsonFeedConsumer.readInlineCount(reader, feedMetadata);
        } else {
          throw new EntityProviderException(EntityProviderException.INVALID_CONTENT.addContent(FormatJson.COUNT).addContent(nextName));
        }
      }
      for (; openedObjects > 0; openedObjects--) {
        reader.endObject();
      }

      reader.peek(); // to assert end of document
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    } catch (final IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }

    return links;
  }

  private List<String> readLinksArray(final JsonReader reader) throws IOException, EntityProviderException {
    List<String> links = new ArrayList<String>();

    reader.beginArray();
    while (reader.hasNext()) {
      reader.beginObject();
      String nextName = reader.nextName();
      if (FormatJson.URI.equals(nextName) && reader.peek() == JsonToken.STRING) {
        links.add(reader.nextString());
      } else {
        throw new EntityProviderException(EntityProviderException.INVALID_CONTENT.addContent(FormatJson.URI).addContent(nextName));
      }
      reader.endObject();
    }
    reader.endArray();

    return links;
  }
}
