package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;

import org.junit.Test;

import com.google.gson.stream.JsonReader;
import com.sap.core.odata.api.ep.EntityProviderException;

/**
 * @author SAP AG
 */
public class JsonLinkConsumerTest extends AbstractConsumerTest {

  @Test
  public void linkWithD() throws Exception {
    final String link = "{\"d\":{\"uri\":\"http://somelink\"}}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(link)));
    assertEquals("http://somelink", new JsonLinkConsumer().readLink(reader, null));
  }

  @Test
  public void linkWithoutD() throws Exception {
    final String link = "{\"uri\":\"http://somelink\"}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(link)));
    assertEquals("http://somelink", new JsonLinkConsumer().readLink(reader, null));
  }

  @Test(expected = EntityProviderException.class)
  public void invalidDoubleClosingBrackets() throws Exception {
    final String link = "{\"uri\":\"http://somelink\"}}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(link)));
    new JsonLinkConsumer().readLink(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void trailingGarbage() throws Exception {
    final String link = "{\"uri\":\"http://somelink\"},{\"a\":null}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(link)));
    new JsonLinkConsumer().readLink(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongTagName() throws Exception {
    final String link = "{\"URI\":\"http://somelink\"}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(link)));
    new JsonLinkConsumer().readLink(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongValueType() throws Exception {
    final String link = "{\"uri\":false}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(link)));
    new JsonLinkConsumer().readLink(reader, null);
  }
}
