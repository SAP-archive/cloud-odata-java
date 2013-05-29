package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;

/**
 * @author SAP AG
 */
public class JsonEntityConsumer {

  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "UTF-8";

  public ODataEntry readEntry(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties properties) throws EntityProviderException {
    JsonReader reader = null;
    EntityProviderException cachedException = null;

    try {
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      reader = createJsonReader(content);

      JsonEntryConsumer jec = new JsonEntryConsumer(reader, eia, properties);
      ODataEntry result = jec.readSingleEntry();
      return result;
    } catch (UnsupportedEncodingException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public ODataFeed readFeed(final EdmEntitySet entitySet, final InputStream content, final EntityProviderReadProperties readProperties) throws EntityProviderException {
    JsonReader reader = null;
    EntityProviderException cachedException = null;

    try {
      EntityInfoAggregator eia = EntityInfoAggregator.create(entitySet);
      reader = createJsonReader(content);

      JsonFeedConsumer jfc = new JsonFeedConsumer(reader, eia, readProperties);
      ODataFeed result = jfc.readFeedStandalone();

      return result;
    } catch (UnsupportedEncodingException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public Map<String, Object> readProperty(final EdmProperty property, final InputStream content, final EntityProviderReadProperties readProperties) throws EntityProviderException {
    JsonReader reader = null;
    EntityProviderException cachedException = null;

    try {
      reader = createJsonReader(content);
      return new JsonPropertyConsumer().readPropertyStandalone(reader, property, readProperties);
    } catch (final UnsupportedEncodingException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
        }
      }
    }
  }

  public String readLink(final EdmEntitySet entitySet, Object content) throws EntityProviderException {
    JsonReader reader = null;
    EntityProviderException cachedException = null;

    try {
      reader = createJsonReader(content);
      return new JsonLinkConsumer().readLink(reader, entitySet);
    } catch (final UnsupportedEncodingException e) {
      cachedException = new EntityProviderException(EntityProviderException.COMMON, e);
      throw cachedException;
    } finally {// NOPMD (suppress DoNotThrowExceptionInFinally)
      if (reader != null) {
        try {
          reader.close();
        } catch (final IOException e) {
          if (cachedException != null) {
            throw cachedException;
          } else {
            throw new EntityProviderException(EntityProviderException.COMMON, e);
          }
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
