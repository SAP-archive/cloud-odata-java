package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;

public class JsonEntityConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(XmlEntityConsumer.class);
  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    JsonReader reader = null;

    try {
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      reader = createJsonReader(content);
      
      JsonEntryConsumer jec = new JsonEntryConsumer(reader, eia, properties);
      ODataEntry result = jec.readEntryStandalone();
      
      return result;
    } catch (UnsupportedEncodingException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }
  
  public ODataFeed readFeed(EdmEntitySet entitySet, InputStream content, EntityProviderReadProperties readProperties) throws EntityProviderException {
    JsonReader reader = null;

    try {
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      reader = createJsonReader(content);
      
      JsonFeedConsumer jfc = new JsonFeedConsumer(reader, eia, readProperties);
      ODataFeed result = jfc.readFeedStandalone();
      
      return result;
    } catch (UnsupportedEncodingException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          // don't throw in finally!
          LOG.error(e.getLocalizedMessage(), e);
        }
      }
    }
  }

  private JsonReader createJsonReader(final Object content) throws EntityProviderException, UnsupportedEncodingException {

    if (content == null) {
      throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
          .addContent("Got not supported NULL object as content to de-serialize."));
    }

    if (content instanceof InputStream) {
      return new JsonReader(new InputStreamReader((InputStream) content, DEFAULT_CHARSET));
    }
    throw new EntityProviderException(EntityProviderException.ILLEGAL_ARGUMENT
        .addContent("Found not supported content of class '" + content.getClass() + "' to de-serialize."));
  }
}
