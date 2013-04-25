package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.ep.entry.ODataFeed;

public class JsonEntryDeepInsertFeedTest extends AbstractConsumerTest {

  private static final String BUILDING_WITH_INLINE_ROOMS = "JsonBuildingWithInlineRooms";
  
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
  
}
