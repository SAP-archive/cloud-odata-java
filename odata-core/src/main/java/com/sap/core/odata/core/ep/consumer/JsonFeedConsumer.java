package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;
import com.sap.core.odata.core.ep.feed.FeedMetadataImpl;
import com.sap.core.odata.core.ep.feed.ODataFeedImpl;
import com.sap.core.odata.core.ep.util.FormatJson;

public class JsonFeedConsumer {

  private JsonReader reader;
  private EntityInfoAggregator eia;
  private EntityProviderReadProperties readProperties;
  private List<ODataEntry> entries = new ArrayList<ODataEntry>();
  private FeedMetadataImpl feedMetadata = new FeedMetadataImpl();
  private boolean resultsArrayPresent = false;

  public JsonFeedConsumer(JsonReader reader, EntityInfoAggregator eia, EntityProviderReadProperties readProperties) {
    this.reader = reader;
    this.eia = eia;
    this.readProperties = readProperties;

  }

  public ODataFeed readFeedStandalone() throws EntityProviderException {
    try {
      reader.beginObject();
      String nextName = reader.nextName();
      if (FormatJson.D.equals(nextName)) {
        reader.beginObject();
        readFeedContent();
        reader.endObject();
      } else {
        handleName(nextName);
        readFeedContent();
      }

      reader.endObject();
    } catch (IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (EdmException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }
    return new ODataFeedImpl(entries, feedMetadata);
  }

  private void readFeedContent() throws IOException, EdmException, EntityProviderException {
    String nextName;
    while (reader.hasNext()) {
      nextName = reader.nextName();
      handleName(nextName);
    }
    
    if(!resultsArrayPresent){
      //TODO: Messagetext
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
  }

  private void handleName(String nextName) throws IOException, EdmException, EntityProviderException {
    if (FormatJson.RESULTS.equals(nextName)) {
      reader.beginArray();
      while (reader.hasNext()) {
        JsonEntryConsumer jec = new JsonEntryConsumer(reader, eia, readProperties);
        ODataEntry entry = jec.readFeedEntry();
        entries.add(entry);
      }
      reader.endArray();
      resultsArrayPresent  = true;
    } else if (FormatJson.COUNT.equals(nextName)) {
      int inlineCount = reader.nextInt();
      feedMetadata.setInlineCount(inlineCount);
    } else if (FormatJson.NEXT.equals(nextName)) {
      String nextLink = reader.nextString();
      feedMetadata.setNextLink(nextLink);
    } else {
      //TODO: CA Messagetext
      throw new EntityProviderException(EntityProviderException.COMMON);
    }
  }

  public ODataFeed readInlineFeed(String name) throws EdmException, EntityProviderException, IOException {
    //consume the already started content
    handleName(name);
    //consume the rest of the entry content
    readFeedContent();
    return new ODataFeedImpl(entries, feedMetadata);
  }

}
