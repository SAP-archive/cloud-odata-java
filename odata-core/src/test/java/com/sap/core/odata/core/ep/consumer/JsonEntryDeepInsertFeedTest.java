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
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.OnReadInlineContent;
import com.sap.core.odata.api.ep.callback.ReadEntryResult;
import com.sap.core.odata.api.ep.callback.ReadFeedResult;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.ep.feed.FeedMetadata;
import com.sap.core.odata.api.ep.feed.ODataFeed;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonEntryDeepInsertFeedTest extends AbstractConsumerTest {

  private static final String BUILDING_WITH_INLINE_ROOMS = "JsonBuildingWithInlineRooms";
  private static final String TEAM_WITH_INLINE_EMPLOYEES = "JsonTeamsWithInlineEmployees";
  private static final String BUILDING_WITH_INLINE_ROOMS_NEXTLINK_AND_COUNT = "JsonBuildingWithInlineRoomsAndNextLinkAndCount";

  @Test
  public void innerFeedNoMediaResourceWithoutCallback() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(BUILDING_WITH_INLINE_ROOMS, "Buildings", DEFAULT_PROPERTIES);

    ODataFeed innerRoomFeed = (ODataFeed) outerEntry.getProperties().get("nb_Rooms");
    assertNotNull(innerRoomFeed);

    List<ODataEntry> rooms = innerRoomFeed.getEntries();
    assertNotNull(rooms);
    assertEquals(1, rooms.size());

    ODataEntry room = rooms.get(0);
    Map<String, Object> roomProperties = room.getProperties();

    assertEquals(4, roomProperties.size());
    assertEquals("1", roomProperties.get("Id"));
    assertEquals("Room 1", roomProperties.get("Name"));
    assertEquals(Short.valueOf("1"), roomProperties.get("Version"));
    assertEquals(Short.valueOf("1"), roomProperties.get("Seats"));

    List<String> associationUris = room.getMetadata().getAssociationUris("nr_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Employees", associationUris.get(0));

    associationUris = room.getMetadata().getAssociationUris("nr_Building");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Building", associationUris.get(0));
  }

  @Test
  public void innerFeedNoMediaResourceWithoutCallbackContainsNextLinkAndCount() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(BUILDING_WITH_INLINE_ROOMS_NEXTLINK_AND_COUNT, "Buildings", DEFAULT_PROPERTIES);

    ODataFeed innerRoomFeed = (ODataFeed) outerEntry.getProperties().get("nb_Rooms");
    assertNotNull(innerRoomFeed);

    List<ODataEntry> rooms = innerRoomFeed.getEntries();
    assertNotNull(rooms);
    assertEquals(1, rooms.size());

    FeedMetadata roomsMetadata = innerRoomFeed.getFeedMetadata();
    assertEquals(Integer.valueOf(1), roomsMetadata.getInlineCount());
    assertEquals("nextLink", roomsMetadata.getNextLink());
  }

  @Test
  public void innerFeedNoMediaResourceWithoutCallbackSimpleArray() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Buildings");
    String content = "{\"d\":{\"Id\":\"1\",\"Name\":\"Building 1\","
        + "\"nb_Rooms\":[{\"Id\":\"1\",\"Name\":\"Room 1\"}]}}";
    InputStream contentBody = createContentAsStream(content);
    final ODataEntry outerEntry = new JsonEntityConsumer().readEntry(entitySet, contentBody, DEFAULT_PROPERTIES);
    assertNotNull(outerEntry);
    final ODataFeed innerRoomFeed = (ODataFeed) outerEntry.getProperties().get("nb_Rooms");
    assertNotNull(innerRoomFeed);

    final List<ODataEntry> rooms = innerRoomFeed.getEntries();
    assertNotNull(rooms);
    assertEquals(1, rooms.size());
  }

  @Test
  public void innerFeedMediaResourceWithoutCallback() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(TEAM_WITH_INLINE_EMPLOYEES, "Teams", DEFAULT_PROPERTIES);

    ODataFeed innerEmployeeFeed = (ODataFeed) outerEntry.getProperties().get("nt_Employees");
    assertNotNull(innerEmployeeFeed);

    List<ODataEntry> employees = innerEmployeeFeed.getEntries();
    assertNotNull(employees);
    assertEquals(3, employees.size());
  }

  @Test
  public void innerFeedNoMediaResourceWithCallback() throws Exception {
    FeedCallback callback = new FeedCallback();
    EntityProviderReadProperties readProperties = EntityProviderReadProperties.init().mergeSemantic(false).callback(callback).build();
    ODataEntry outerEntry = prepareAndExecuteEntry(BUILDING_WITH_INLINE_ROOMS, "Buildings", readProperties);

    ODataFeed innerRoomFeed = (ODataFeed) outerEntry.getProperties().get("nb_Rooms");
    assertNull(innerRoomFeed);

    innerRoomFeed = callback.getFeed();

    List<ODataEntry> rooms = innerRoomFeed.getEntries();
    assertNotNull(rooms);
    assertEquals(1, rooms.size());

    ODataEntry room = rooms.get(0);
    Map<String, Object> roomProperties = room.getProperties();

    assertEquals(4, roomProperties.size());
    assertEquals("1", roomProperties.get("Id"));
    assertEquals("Room 1", roomProperties.get("Name"));
    assertEquals(Short.valueOf("1"), roomProperties.get("Version"));
    assertEquals(Short.valueOf("1"), roomProperties.get("Seats"));

    List<String> associationUris = room.getMetadata().getAssociationUris("nr_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Employees", associationUris.get(0));

    associationUris = room.getMetadata().getAssociationUris("nr_Building");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Building", associationUris.get(0));
  }

  @Test
  public void innerFeedNoMediaResourceWithCallbackContainsNextLinkAndCount() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(BUILDING_WITH_INLINE_ROOMS_NEXTLINK_AND_COUNT, "Buildings", DEFAULT_PROPERTIES);

    ODataFeed innerRoomFeed = (ODataFeed) outerEntry.getProperties().get("nb_Rooms");
    assertNotNull(innerRoomFeed);

    List<ODataEntry> rooms = innerRoomFeed.getEntries();
    assertNotNull(rooms);
    assertEquals(1, rooms.size());

    FeedMetadata roomsMetadata = innerRoomFeed.getFeedMetadata();
    assertEquals(Integer.valueOf(1), roomsMetadata.getInlineCount());
    assertEquals("nextLink", roomsMetadata.getNextLink());

  }

  @Test
  public void innerFeedMediaResourceWithCallback() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(TEAM_WITH_INLINE_EMPLOYEES, "Teams", DEFAULT_PROPERTIES);

    ODataFeed innerEmployeeFeed = (ODataFeed) outerEntry.getProperties().get("nt_Employees");
    assertNotNull(innerEmployeeFeed);

    List<ODataEntry> employees = innerEmployeeFeed.getEntries();
    assertNotNull(employees);
    assertEquals(3, employees.size());
  }

  private class FeedCallback implements OnReadInlineContent {
    private ODataFeed feed;
    private FeedCallback innerCallback;

    public FeedCallback() {

    }

    public ODataFeed getFeed() {
      return feed;
    }

    @Override
    public void handleReadEntry(final ReadEntryResult context) {
      throw new ODataRuntimeException("No entry expected");
    }

    @Override
    public void handleReadFeed(final ReadFeedResult context) {
      feed = context.getResult();
    }

    @Override
    public EntityProviderReadProperties receiveReadProperties(final EntityProviderReadProperties readProperties, final EdmNavigationProperty navString) {
      return EntityProviderReadProperties.init().mergeSemantic(false).callback(innerCallback).build();
    }
  }

}
