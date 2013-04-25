package com.sap.core.odata.core.ep.consumer;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.OnReadInlineContent;
import com.sap.core.odata.api.ep.callback.ReadEntryResult;
import com.sap.core.odata.api.ep.callback.ReadFeedResult;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.testutil.mock.MockFacade;

public class JsonEntityConsumerDeepInsertTest extends AbstractConsumerTest {

  private static final int EMPLOYEE_WITH_INLINE_TEAM = 0;
  private static final int INLINE_ROOM_WITH_INLINE_BUILDING = 1;
  private static final EntityProviderReadProperties DEFAULT_PROPERTIES = EntityProviderReadProperties.init().mergeSemantic(false).build();

  @Test
  public void innerEntryNoMediaResourceWithoutCallback() throws Exception {
    ODataEntry outerEntry = prepareAndExecute(EMPLOYEE_WITH_INLINE_TEAM, DEFAULT_PROPERTIES);

    ODataEntry innerTeam = (ODataEntry) outerEntry.getProperties().get("ne_Team");

    assertNotNull(innerTeam);
    Map<String, Object> innerTeamProperties = innerTeam.getProperties();

    assertEquals("1", innerTeamProperties.get("Id"));
    assertEquals("Team 1", innerTeamProperties.get("Name"));
    assertEquals(Boolean.FALSE, innerTeamProperties.get("isScrumTeam"));
    assertNull(innerTeamProperties.get("nt_Employees"));

    List<String> associationUris = innerTeam.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));
  }

  @Test
  public void innerEntryNoMediaResourceWithCallback() throws Exception {
    EntryCallback callback = new EntryCallback();
    EntityProviderReadProperties readProperties = EntityProviderReadProperties.init().mergeSemantic(false).callback(callback).build();
    ODataEntry outerEntry = prepareAndExecute(EMPLOYEE_WITH_INLINE_TEAM, readProperties);

    assertThat(outerEntry.getProperties().get("ne_Team"), nullValue());

    ODataEntry innerTeam = callback.getEntry();
    Map<String, Object> innerTeamProperties = innerTeam.getProperties();

    assertEquals("1", innerTeamProperties.get("Id"));
    assertEquals("Team 1", innerTeamProperties.get("Name"));
    assertEquals(Boolean.FALSE, innerTeamProperties.get("isScrumTeam"));
    assertNull(innerTeamProperties.get("nt_Employees"));

    List<String> associationUris = innerTeam.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));
  }

  @Test
  public void inlineRoomWithInlineBuilding() throws Exception {
    ODataEntry outerEntry = prepareAndExecute(INLINE_ROOM_WITH_INLINE_BUILDING, DEFAULT_PROPERTIES);

    ODataEntry innerRoom = (ODataEntry) outerEntry.getProperties().get("ne_Room");
    assertNotNull(innerRoom);

    Map<String, Object> innerRoomProperties = innerRoom.getProperties();

    assertEquals(5, innerRoomProperties.size());
    assertEquals("1", innerRoomProperties.get("Id"));
    assertEquals("Room 1", innerRoomProperties.get("Name"));
    assertEquals(Short.valueOf("1"), innerRoomProperties.get("Version"));
    assertEquals(Short.valueOf("1"), innerRoomProperties.get("Seats"));
    assertNull(innerRoomProperties.get("nr_Employees"));

    List<String> associationUris = innerRoom.getMetadata().getAssociationUris("nr_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Employees", associationUris.get(0));

    associationUris = innerRoom.getMetadata().getAssociationUris("nr_Building");
    assertEquals(Collections.emptyList(), associationUris);

    ODataEntry innerBuilding = (ODataEntry) innerRoomProperties.get("nr_Building");
    assertNotNull(innerBuilding);

    Map<String, Object> innerBuildingProperties = innerBuilding.getProperties();
    assertEquals(3, innerBuildingProperties.size());
    assertEquals("1", innerBuildingProperties.get("Id"));
    assertEquals("Building 1", innerBuildingProperties.get("Name"));
    assertEquals(null, innerBuildingProperties.get("Image"));
    assertNull(innerBuildingProperties.get("nb_Rooms"));

    associationUris = innerBuilding.getMetadata().getAssociationUris("nb_Rooms");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Buildings('1')/nb_Rooms", associationUris.get(0));
  }

  @Test
  public void inlineRoomWithInlineBuildingWithRoomCallback() throws Exception {
    EntryCallback callback = new EntryCallback();
    EntityProviderReadProperties readProperties = EntityProviderReadProperties.init().mergeSemantic(false).callback(callback).build();
    ODataEntry outerEntry = prepareAndExecute(INLINE_ROOM_WITH_INLINE_BUILDING, readProperties);

    ODataEntry innerRoom = (ODataEntry) outerEntry.getProperties().get("ne_Room");
    assertNull(innerRoom);

    innerRoom = callback.getEntry();

    Map<String, Object> innerRoomProperties = innerRoom.getProperties();

    assertEquals(5, innerRoomProperties.size());
    assertEquals("1", innerRoomProperties.get("Id"));
    assertEquals("Room 1", innerRoomProperties.get("Name"));
    assertEquals(Short.valueOf("1"), innerRoomProperties.get("Version"));
    assertEquals(Short.valueOf("1"), innerRoomProperties.get("Seats"));
    assertNull(innerRoomProperties.get("nr_Employees"));

    List<String> associationUris = innerRoom.getMetadata().getAssociationUris("nr_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Employees", associationUris.get(0));

    associationUris = innerRoom.getMetadata().getAssociationUris("nr_Building");
    assertEquals(Collections.emptyList(), associationUris);

    ODataEntry innerBuilding = (ODataEntry) innerRoomProperties.get("nr_Building");
    assertNotNull(innerBuilding);

    Map<String, Object> innerBuildingProperties = innerBuilding.getProperties();
    assertEquals(3, innerBuildingProperties.size());
    assertEquals("1", innerBuildingProperties.get("Id"));
    assertEquals("Building 1", innerBuildingProperties.get("Name"));
    assertEquals(null, innerBuildingProperties.get("Image"));
    assertNull(innerBuildingProperties.get("nb_Rooms"));

    associationUris = innerBuilding.getMetadata().getAssociationUris("nb_Rooms");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Buildings('1')/nb_Rooms", associationUris.get(0));
  }

  @Test
  public void inlineRoomWithInlineBuildingWithCallbacks() throws Exception {
    EntryCallback buildingCallback = new EntryCallback();
    EntryCallback roomCallback = new EntryCallback(buildingCallback);
    EntityProviderReadProperties readProperties = EntityProviderReadProperties.init().mergeSemantic(false).callback(roomCallback).build();
    ODataEntry outerEntry = prepareAndExecute(INLINE_ROOM_WITH_INLINE_BUILDING, readProperties);

    ODataEntry innerRoom = (ODataEntry) outerEntry.getProperties().get("ne_Room");
    assertNull(innerRoom);

    innerRoom = roomCallback.getEntry();

    Map<String, Object> innerRoomProperties = innerRoom.getProperties();

    assertEquals(4, innerRoomProperties.size());
    assertEquals("1", innerRoomProperties.get("Id"));
    assertEquals("Room 1", innerRoomProperties.get("Name"));
    assertEquals(Short.valueOf("1"), innerRoomProperties.get("Version"));
    assertEquals(Short.valueOf("1"), innerRoomProperties.get("Seats"));
    assertNull(innerRoomProperties.get("nr_Employees"));

    List<String> associationUris = innerRoom.getMetadata().getAssociationUris("nr_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Rooms('1')/nr_Employees", associationUris.get(0));

    associationUris = innerRoom.getMetadata().getAssociationUris("nr_Building");
    assertEquals(Collections.emptyList(), associationUris);

    ODataEntry innerBuilding = (ODataEntry) innerRoomProperties.get("nr_Building");
    assertNull(innerBuilding);
    
    innerBuilding = buildingCallback.getEntry();

    Map<String, Object> innerBuildingProperties = innerBuilding.getProperties();
    assertEquals(3, innerBuildingProperties.size());
    assertEquals("1", innerBuildingProperties.get("Id"));
    assertEquals("Building 1", innerBuildingProperties.get("Name"));
    assertEquals(null, innerBuildingProperties.get("Image"));
    assertNull(innerBuildingProperties.get("nb_Rooms"));

    associationUris = innerBuilding.getMetadata().getAssociationUris("nb_Rooms");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Buildings('1')/nb_Rooms", associationUris.get(0));
  }

  private class EntryCallback implements OnReadInlineContent {
    private ODataEntry entry;
    private EntryCallback innerCallback;

    public EntryCallback(EntryCallback innerCallback) {
      this.innerCallback = innerCallback;
    }

    public EntryCallback() {

    }

    public ODataEntry getEntry() {
      return entry;
    }

    @Override
    public void handleReadEntry(final ReadEntryResult context) {
      entry = context.getResult();
    }

    @Override
    public void handleReadFeed(final ReadFeedResult context) {
      throw new ODataRuntimeException("No feed expected");
    }

    @Override
    public EntityProviderReadProperties receiveReadProperties(final EntityProviderReadProperties readProperties, final EdmNavigationProperty navString) {
      return EntityProviderReadProperties.init().mergeSemantic(false).callback(innerCallback).build();
    }
  }

  private ODataEntry prepareAndExecute(final int entryIdentificator, EntityProviderReadProperties readProperties) throws IOException, EdmException, ODataException, UnsupportedEncodingException, EntityProviderException {
    EdmEntitySet entitySet = null;
    String content = null;
    switch (entryIdentificator) {
    case EMPLOYEE_WITH_INLINE_TEAM:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
      content = readFile("JsonEmployeeWithInlineTeam");
      break;
    case INLINE_ROOM_WITH_INLINE_BUILDING:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
      content = readFile("JsonInlineRoomWithInlineBuilding");
      break;

    default:
      fail("Invalid entryIdentificator: " + entryIdentificator);
    }

    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, readProperties);
    assertNotNull(result);
    return result;
  }
}
