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
    assertEquals(0, feedMetadata.getInlineCount());
    assertEquals("", feedMetadata.getNextLink());
  }

  @Test
  public void emptyFeed() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    String content = "{\"d\":{\"results\":[]}}";
    assertNotNull(content);
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
    assertEquals(0, feedMetadata.getInlineCount());
    assertEquals("", feedMetadata.getNextLink());
  }

  @Test(expected = EntityProviderException.class)
  public void resultsNotPresent() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    String content = "{\"d\":{}}";
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    xec.readFeed(entitySet, contentBody, DEFAULT_PROPERTIES);
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
    assertEquals(3, feedMetadata.getInlineCount());
  }

  @Test
  public void feedWithInlineCountAndNext() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    String content = "{\"d\":{\"__count\":\"3\",\"results\":[{\"__metadata\":{\"id\":\"http://localhost:8080/ReferenceScenario.svc/Teams('1')\",\"uri\":\"http://localhost:8080/ReferenceScenario.svc/Teams('1')\",\"type\":\"RefScenario.Team\"},\"Id\":\"1\",\"Name\":\"Team 1\",\"isScrumTeam\":false,\"nt_Employees\":{\"__deferred\":{\"uri\":\"http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees\"}}}],\"__next\":\"Rooms?$skiptoken=98&$inlinecount=allpages\"}}";
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
    assertEquals(3, feedMetadata.getInlineCount());
    assertEquals("Rooms?$skiptoken=98&$inlinecount=allpages", feedMetadata.getNextLink());
  }

  private void checkMediaDataInitial(final MediaMetadata mediaMetadata) {
    assertNull(mediaMetadata.getContentType());
    assertNull(mediaMetadata.getEditLink());
    assertNull(mediaMetadata.getEtag());
    assertNull(mediaMetadata.getSourceLink());
  }

}
