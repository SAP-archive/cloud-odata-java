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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.FeedMetadata;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonFeedConsumerTest extends AbstractConsumerTest {

  @Test
  public void teamsFeed() throws Exception {
    ODataFeed feed = prepareAndExecuteFeed("JsonTeams", "Teams", DEFAULT_PROPERTIES);

    List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(2, entries.size());

    //Team1
    ODataEntry entry = entries.get(0);
    Map<String, Object> properties = entry.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
    assertNull(properties.get("nt_Employees"));

    List<String> associationUris = entry.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));

    checkMediaDataInitial(entry.getMediaMetadata());

    //Team2
    entry = entries.get(1);
    properties = entry.getProperties();
    assertNotNull(properties);
    assertEquals("2", properties.get("Id"));
    assertEquals("Team 2", properties.get("Name"));
    assertEquals(Boolean.TRUE, properties.get("isScrumTeam"));
    assertNull(properties.get("nt_Employees"));

    associationUris = entry.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('2')/nt_Employees", associationUris.get(0));

    checkMediaDataInitial(entry.getMediaMetadata());

    //Check FeedMetadata
    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertNull(feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test
  public void teamsFeedWithoutD() throws Exception {
    ODataFeed feed = prepareAndExecuteFeed("JsonTeamsWithoutD", "Teams", DEFAULT_PROPERTIES);

    List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(2, entries.size());

    //Team1
    ODataEntry entry = entries.get(0);
    Map<String, Object> properties = entry.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
    assertNull(properties.get("nt_Employees"));

    List<String> associationUris = entry.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));

    checkMediaDataInitial(entry.getMediaMetadata());

    //Team2
    entry = entries.get(1);
    properties = entry.getProperties();
    assertNotNull(properties);
    assertEquals("2", properties.get("Id"));
    assertEquals("Team 2", properties.get("Name"));
    assertEquals(Boolean.TRUE, properties.get("isScrumTeam"));
    assertNull(properties.get("nt_Employees"));

    associationUris = entry.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('2')/nt_Employees", associationUris.get(0));

    checkMediaDataInitial(entry.getMediaMetadata());

    //Check FeedMetadata
    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertNull(feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test(expected = EntityProviderException.class)
  public void invalidDoubleClosingBrackets() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    String content = "{\"d\":{\"results\":[]}}}";
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    xec.readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test
  public void emptyFeed() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    String content = "{\"d\":{\"results\":[]}}";
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataFeed feed = xec.readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
    assertNotNull(feed);

    List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(0, entries.size());

    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertNull(feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test
  public void emptyFeedWithoutDAndResults() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("[]");
    final ODataFeed feed = new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
    assertNotNull(feed);
    final List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(0, entries.size());
    final FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertNull(feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test
  public void emptyFeedWithoutResults() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":[]}");
    final ODataFeed feed = new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
    assertNotNull(feed);
    final List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(0, entries.size());
    final FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertNull(feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test(expected = EntityProviderException.class)
  public void resultsNotPresent() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void countButNoResults() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"__count\":\"1\"}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongCountType() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"__count\":1,\"results\":[]}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongCountContent() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"__count\":\"one\",\"results\":[]}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void negativeCount() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"__count\":\"-1\",\"results\":[]}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongNextType() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"results\":[],\"__next\":false}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongTag() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"__results\":null}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void doubleCount() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"__count\":\"1\",\"__count\":\"2\",\"results\":[]}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void doubleNext() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"results\":[],\"__next\":\"a\",\"__next\":\"b\"}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void doubleResults() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"results\":{\"results\":[]}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void doubleD() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream("{\"d\":{\"d\":[]}}");
    new JsonEntityConsumer().readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test
  public void teamsFeedWithCount() throws Exception {
    ODataFeed feed = prepareAndExecuteFeed("JsonTeamsWithCount", "Teams", DEFAULT_PROPERTIES);

    List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(2, entries.size());

    //Check FeedMetadata
    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertEquals(Integer.valueOf(3), feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test
  public void teamsFeedWithCountWithoutD() throws Exception {
    ODataFeed feed = prepareAndExecuteFeed("JsonTeamsWithCountWithoutD", "Teams", DEFAULT_PROPERTIES);

    List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(2, entries.size());

    //Check FeedMetadata
    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertEquals(Integer.valueOf(3), feedMetadata.getInlineCount());
    assertNull(feedMetadata.getNextLink());
  }

  @Test
  public void feedWithInlineCountAndNextAndDelta() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    String content = "{\"d\":{\"__count\":\"3\",\"results\":[{\"__metadata\":{\"id\":\"http://localhost:8080/ReferenceScenario.svc/Teams('1')\",\"uri\":\"http://localhost:8080/ReferenceScenario.svc/Teams('1')\",\"type\":\"RefScenario.Team\"},\"Id\":\"1\",\"Name\":\"Team 1\",\"isScrumTeam\":false,\"nt_Employees\":{\"__deferred\":{\"uri\":\"http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees\"}}}],\"__next\":\"Rooms?$skiptoken=98&$inlinecount=allpages\",\"__delta\":\"deltalink\"}}";
    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataFeed feed = xec.readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
    assertNotNull(feed);

    List<ODataEntry> entries = feed.getEntries();
    assertNotNull(entries);
    assertEquals(1, entries.size());

    FeedMetadata feedMetadata = feed.getFeedMetadata();
    assertNotNull(feedMetadata);
    assertEquals(Integer.valueOf(3), feedMetadata.getInlineCount());
    assertEquals("Rooms?$skiptoken=98&$inlinecount=allpages", feedMetadata.getNextLink());
    assertEquals("deltalink", feedMetadata.getDeltaLink());
  }

  private void checkMediaDataInitial(final MediaMetadata mediaMetadata) {
    assertNull(mediaMetadata.getContentType());
    assertNull(mediaMetadata.getEditLink());
    assertNull(mediaMetadata.getEtag());
    assertNull(mediaMetadata.getSourceLink());
  }

}
