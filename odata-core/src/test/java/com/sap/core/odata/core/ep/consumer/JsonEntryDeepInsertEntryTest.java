package com.sap.core.odata.core.ep.consumer;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.OnReadInlineContent;
import com.sap.core.odata.api.ep.callback.ReadEntryResult;
import com.sap.core.odata.api.ep.callback.ReadFeedResult;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.exception.ODataRuntimeException;
import com.sap.core.odata.testutil.mock.MockFacade;

public class JsonEntryDeepInsertEntryTest extends AbstractConsumerTest {

  private static final String EMPLOYEE_WITH_INLINE_TEAM = "JsonEmployeeWithInlineTeam";
  private static final String INLINE_ROOM_WITH_INLINE_BUILDING = "JsonInlineRoomWithInlineBuilding";

  @Test
  public void innerEntryNoMediaResourceWithoutCallback() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(EMPLOYEE_WITH_INLINE_TEAM, "Employees", DEFAULT_PROPERTIES);
    assertTrue(outerEntry.containsInlineEntry());

    ODataEntry innerTeam = (ODataEntry) outerEntry.getProperties().get("ne_Team");
    assertNotNull(innerTeam);
    assertFalse(innerTeam.containsInlineEntry());

    Map<String, Object> innerTeamProperties = innerTeam.getProperties();

    assertEquals("1", innerTeamProperties.get("Id"));
    assertEquals("Team 1", innerTeamProperties.get("Name"));
    assertEquals(Boolean.FALSE, innerTeamProperties.get("isScrumTeam"));
    assertNull(innerTeamProperties.get("nt_Employees"));

    List<String> associationUris = innerTeam.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));
  }

  @Test
  public void innerEntryNoMediaResourceWithCallback() throws Exception {
    EntryCallback callback = new EntryCallback();
    EntityProviderReadProperties readProperties = EntityProviderReadProperties.init().mergeSemantic(false).callback(callback).build();
    ODataEntry outerEntry = prepareAndExecuteEntry(EMPLOYEE_WITH_INLINE_TEAM, "Employees", readProperties);

    assertThat(outerEntry.getProperties().get("ne_Team"), nullValue());

    ODataEntry innerTeam = callback.getEntry();
    Map<String, Object> innerTeamProperties = innerTeam.getProperties();

    assertEquals("1", innerTeamProperties.get("Id"));
    assertEquals("Team 1", innerTeamProperties.get("Name"));
    assertEquals(Boolean.FALSE, innerTeamProperties.get("isScrumTeam"));
    assertNull(innerTeamProperties.get("nt_Employees"));

    List<String> associationUris = innerTeam.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));
  }

  @Test
  public void innerEntryWithOptionalNavigationProperty() throws Exception {
    // XXX: mibo
    EntryCallback callback = new EntryCallback();
    EntityProviderReadProperties readProperties = EntityProviderReadProperties.init().mergeSemantic(false).callback(callback).build();
    //prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    //
    EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeKind.ENTITY);

    EdmNavigationProperty navigationProperty = mock(EdmNavigationProperty.class);
    when(navigationProperty.getName()).thenReturn("ne_Team");
    when(navigationProperty.getType()).thenReturn(navigationType);
    when(navigationProperty.getMultiplicity()).thenReturn(EdmMultiplicity.ZERO_TO_ONE);

    when(entitySet.getEntityType().getProperty("ne_Team")).thenReturn(navigationProperty);
    EdmEntitySet targetEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    when(entitySet.getRelatedEntitySet(navigationProperty)).thenReturn(targetEntitySet);
    //
    String content = readFile(EMPLOYEE_WITH_INLINE_TEAM);
    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);
    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataEntry outerEntry = xec.readEntry(entitySet, contentBody, readProperties);

    //
    assertThat(outerEntry.getProperties().get("ne_Team"), nullValue());

    ODataEntry innerTeam = callback.getEntry();
    Map<String, Object> innerTeamProperties = innerTeam.getProperties();

    assertEquals("1", innerTeamProperties.get("Id"));
    assertEquals("Team 1", innerTeamProperties.get("Name"));
    assertEquals(Boolean.FALSE, innerTeamProperties.get("isScrumTeam"));
    assertNull(innerTeamProperties.get("nt_Employees"));

    List<String> associationUris = innerTeam.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));
  }

  @Test
  public void inlineRoomWithInlineBuildingNoCallback() throws Exception {
    ODataEntry outerEntry = prepareAndExecuteEntry(INLINE_ROOM_WITH_INLINE_BUILDING, "Employees", DEFAULT_PROPERTIES);
    assertTrue(outerEntry.containsInlineEntry());

    ODataEntry innerRoom = (ODataEntry) outerEntry.getProperties().get("ne_Room");
    assertNotNull(innerRoom);
    assertTrue(innerRoom.containsInlineEntry());

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
    assertFalse(innerBuilding.containsInlineEntry());

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
    ODataEntry outerEntry = prepareAndExecuteEntry(INLINE_ROOM_WITH_INLINE_BUILDING, "Employees", readProperties);

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
    ODataEntry outerEntry = prepareAndExecuteEntry(INLINE_ROOM_WITH_INLINE_BUILDING, "Employees", readProperties);

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

    public EntryCallback(final EntryCallback innerCallback) {
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
}
