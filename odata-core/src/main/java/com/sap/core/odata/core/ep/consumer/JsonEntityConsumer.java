/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.core.ep.consumer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.ep.aggregator.EntityInfoAggregator;

public class JsonEntityConsumer {

  /** Default used charset for writer and response content header */
  private static final String DEFAULT_CHARSET = "utf-8";

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
    } finally {
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
    } finally {
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
