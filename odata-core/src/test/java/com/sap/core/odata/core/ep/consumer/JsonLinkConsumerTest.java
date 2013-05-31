package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

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

  @Test
  public void linksWithD() throws Exception {
    final String links = "{\"d\":[{\"uri\":\"http://somelink\"}]}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    assertEquals(Arrays.asList("http://somelink"), new JsonLinkConsumer().readLinks(reader, null));
  }

  @Test
  public void linksWithoutD() throws Exception {
    final String links = "[{\"uri\":\"http://somelink\"},{\"uri\":\"\"}]";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    assertEquals(Arrays.asList("http://somelink", ""), new JsonLinkConsumer().readLinks(reader, null));
  }

  @Test
  public void linksEmpty() throws Exception {
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream("[]")));
    assertEquals(Collections.emptyList(), new JsonLinkConsumer().readLinks(reader, null));
  }

  @Test
  public void linksWithCount() throws Exception {
    final String links = "{\"__count\":\"5\",\"results\":[{\"uri\":\"http://somelink\"},{\"uri\":\"\"}]}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    assertEquals(Arrays.asList("http://somelink", ""), new JsonLinkConsumer().readLinks(reader, null));
  }

  @Test
  public void linksWithCountAtEnd() throws Exception {
    final String links = "{\"results\":[{\"uri\":\"http://somelink\"},{\"uri\":\"\"}],\"__count\":\"5\"}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    assertEquals(Arrays.asList("http://somelink", ""), new JsonLinkConsumer().readLinks(reader, null));
  }

  @Test
  public void linksWithDAndResults() throws Exception {
    final String links = "{\"d\":{\"results\":[{\"uri\":\"http://somelink\"}]}}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    assertEquals(Arrays.asList("http://somelink"), new JsonLinkConsumer().readLinks(reader, null));
  }

  @Test(expected = EntityProviderException.class)
  public void linksWrongResultsName() throws Exception {
    final String links = "{\"__results\":[{\"uri\":\"http://somelink\"}]}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void linksWrongCountName() throws Exception {
    final String links = "{\"count\":\"5\",\"results\":[]}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void linksWrongCountNameAtEnd() throws Exception {
    final String links = "{\"results\":[],\"count\":\"5\"}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void linksWithoutResults() throws Exception {
    final String links = "{\"count\":\"42\"}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void linksDoubleCount() throws Exception {
    final String links = "{\"__count\":\"5\",\"results\":[],\"__count\":\"42\"}";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void linksWrongUriName() throws Exception {
    final String links = "[{\"uri\":\"http://somelink\"},{\"URI\":\"\"}]";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }

  @Test(expected = EntityProviderException.class)
  public void linksWrongUriType() throws Exception {
    final String links = "[{\"uri\":false}]";
    JsonReader reader = new JsonReader(new InputStreamReader(createContentAsStream(links)));
    new JsonLinkConsumer().readLinks(reader, null);
  }
}
