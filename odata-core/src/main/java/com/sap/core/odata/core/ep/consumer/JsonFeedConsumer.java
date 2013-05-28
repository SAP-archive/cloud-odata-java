package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.feed.FeedMetadataImpl;
import com.sap.core.odata.core.ep.feed.ODataFeedImpl;
import com.sap.core.odata.core.ep.util.FormatJson;

/**
 * @author SAP AG
 */
public class JsonFeedConsumer {

  private JsonReader reader;
  private EntityInfoAggregator eia;
  private EntityProviderReadProperties readProperties;
  private List<ODataEntry> entries = new ArrayList<ODataEntry>();
  private FeedMetadataImpl feedMetadata = new FeedMetadataImpl();
  private boolean resultsArrayPresent = false;

  public JsonFeedConsumer(final JsonReader reader, final EntityInfoAggregator eia, final EntityProviderReadProperties readProperties) {
    this.reader = reader;
    this.eia = eia;
    this.readProperties = readProperties;
  }

  public ODataFeed readFeedStandalone() throws EntityProviderException {
    try {
      readFeed();

      if (reader.peek() != JsonToken.END_DOCUMENT) {
        //TODO: CA Messagetext
        throw new EntityProviderException(EntityProviderException.COMMON);
      }

    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (IllegalStateException e) {
      throw new EntityProviderException(EntityProviderException.INVALID_STATE.addContent(e.getMessage()), e);
    }
    return new ODataFeedImpl(entries, feedMetadata);
  }

  private void readFeed() throws IOException, EdmException, EntityProviderException {
    if (reader.peek() == JsonToken.BEGIN_ARRAY) {
      readArrayContent();
    } else {
      reader.beginObject();
      final String nextName = reader.nextName();
      if (FormatJson.D.equals(nextName)) {
        if (reader.peek() == JsonToken.BEGIN_ARRAY) {
          readArrayContent();
        } else {
          reader.beginObject();
          readFeedContent();
          reader.endObject();
        }
      } else {
        handleName(nextName);
        readFeedContent();
      }

      reader.endObject();
    }
  }

  private void readFeedContent() throws IOException, EdmException, EntityProviderException {
    while (reader.hasNext()) {
      final String nextName = reader.nextName();
      handleName(nextName);
    }

    if (!resultsArrayPresent) {
      //TODO: Messagetext
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
  }

  private void handleName(final String nextName) throws IOException, EdmException, EntityProviderException {
    if (FormatJson.RESULTS.equals(nextName)) {
      resultsArrayPresent = true;
      readArrayContent();

    } else if (FormatJson.COUNT.equals(nextName)) {
      if (reader.peek() == JsonToken.STRING && feedMetadata.getInlineCount() == null) {
        int inlineCount;
        try {
          inlineCount = reader.nextInt();
        } catch (final NumberFormatException e) {
          throw new EntityProviderException(EntityProviderException.INLINECOUNT_INVALID.addContent(""), e);
        }
        if (inlineCount >= 0) {
          feedMetadata.setInlineCount(inlineCount);
        } else {
          throw new EntityProviderException(EntityProviderException.INLINECOUNT_INVALID.addContent(inlineCount));
        }
      } else {
        throw new EntityProviderException(EntityProviderException.INLINECOUNT_INVALID.addContent(reader.peek()));
      }

    } else if (FormatJson.NEXT.equals(nextName)) {
      if (reader.peek() == JsonToken.STRING && feedMetadata.getNextLink() == null) {
        String nextLink = reader.nextString();
        feedMetadata.setNextLink(nextLink);
      } else {
        //TODO: CA Messagetext
        throw new EntityProviderException(EntityProviderException.COMMON);
      }

    } else {
      //TODO: CA Messagetext
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
  }

  private void readArrayContent() throws IOException, EdmException, EntityProviderException {
    reader.beginArray();
    while (reader.hasNext()) {
      final ODataEntry entry = new JsonEntryConsumer(reader, eia, readProperties).readFeedEntry();
      entries.add(entry);
    }
    reader.endArray();
  }

  protected ODataFeed readStartedInlineFeed(final String name) throws EdmException, EntityProviderException, IOException {
    //consume the already started content
    handleName(name);
    //consume the rest of the entry content
    readFeedContent();
    return new ODataFeedImpl(entries, feedMetadata);
  }

  protected ODataFeed readInlineFeedStandalone() throws EdmException, EntityProviderException, IOException {
    readFeed();
    return new ODataFeedImpl(entries, feedMetadata);
  }

}
