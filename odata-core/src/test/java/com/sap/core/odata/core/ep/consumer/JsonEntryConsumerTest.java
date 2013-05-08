/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonEntryConsumerTest extends AbstractConsumerTest {

  private static final String SIMPLE_ENTRY_BUILDING = "JsonBuilding";
  private static final String SIMPLE_ENTRY_EMPLOYEE = "JsonEmployee";
  private static final String SIMPLE_ENTRY_TEAM = "JsonTeam";
  private static final String INVALID_ENTRY_TEAM_DOUBLE_NAME_PROPERTY = "JsonInvalidTeamDoubleNameProperty";
  private static final String SIMPLE_ENTRY_BUILDING_WITHOUT_D = "JsonBuildingWithoutD";

  //Negative Test jsonStart
  private static final String negativeJsonStart_1 = "{ \"abc\": {";
  private static final String negativeJsonStart_2 = "{ \"d\": [a: 1, b: 2] }";

  @SuppressWarnings("unchecked")
  @Test
  public void readSimpleEmployeeEntry() throws Exception {
    ODataEntry result = prepareAndExecuteEntry(SIMPLE_ENTRY_EMPLOYEE, "Employees", DEFAULT_PROPERTIES);

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(9, properties.size());

    assertEquals("1", properties.get("EmployeeId"));
    assertEquals("Walter Winter", properties.get("EmployeeName"));
    assertEquals("1", properties.get("ManagerId"));
    assertEquals("1", properties.get("RoomId"));
    assertEquals("1", properties.get("TeamId"));
    Map<String, Object> location = (Map<String, Object>) properties.get("Location");
    assertEquals(2, location.size());
    assertEquals("Germany", location.get("Country"));
    Map<String, Object> city = (Map<String, Object>) location.get("City");
    assertEquals(2, city.size());
    assertEquals("69124", city.get("PostalCode"));
    assertEquals("Heidelberg", city.get("CityName"));
    assertEquals(Integer.valueOf(52), properties.get("Age"));
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("Employees('1')/$value", properties.get("ImageUrl"));

    List<String> associationUris = result.getMetadata().getAssociationUris("ne_Manager");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Employees('1')/ne_Manager", associationUris.get(0));

    associationUris = result.getMetadata().getAssociationUris("ne_Team");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Employees('1')/ne_Team", associationUris.get(0));

    associationUris = result.getMetadata().getAssociationUris("ne_Room");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Employees('1')/ne_Room", associationUris.get(0));

    MediaMetadata mediaMetadata = result.getMediaMetadata();
    assertEquals("image/jpeg", mediaMetadata.getContentType());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Employees('1')/$value", mediaMetadata.getEditLink());
    assertEquals("Employees('1')/$value", mediaMetadata.getSourceLink());
    assertNull(mediaMetadata.getEtag());
  }

  @Test
  public void readSimpleTeamEntry() throws Exception {
    ODataEntry result = prepareAndExecuteEntry(SIMPLE_ENTRY_TEAM, "Teams", DEFAULT_PROPERTIES);

    Map<String, Object> properties = result.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
    assertNull(properties.get("nt_Employees"));

    List<String> associationUris = result.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));

    checkMediaDataInitial(result.getMediaMetadata());
  }

  @Test
  public void readSimpleBuildingEntry() throws Exception {
    ODataEntry result = prepareAndExecuteEntry(SIMPLE_ENTRY_BUILDING, "Buildings", DEFAULT_PROPERTIES);
    //verify
    Map<String, Object> properties = result.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Building 1", properties.get("Name"));
    assertEquals(null, properties.get("Image"));
    assertNull(properties.get("nb_Rooms"));

    List<String> associationUris = result.getMetadata().getAssociationUris("nb_Rooms");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Buildings('1')/nb_Rooms", associationUris.get(0));

    checkMediaDataInitial(result.getMediaMetadata());
  }

  @Test
  public void readSimpleBuildingEntryWithoutD() throws Exception {
    ODataEntry result = prepareAndExecuteEntry(SIMPLE_ENTRY_BUILDING_WITHOUT_D, "Buildings", DEFAULT_PROPERTIES);
    //verify
    Map<String, Object> properties = result.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Building 1", properties.get("Name"));
    assertEquals(null, properties.get("Image"));
    assertNull(properties.get("nb_Rooms"));

    List<String> associationUris = result.getMetadata().getAssociationUris("nb_Rooms");
    assertEquals(1, associationUris.size());
    assertEquals("http://localhost:8080/ReferenceScenario.svc/Buildings('1')/nb_Rooms", associationUris.get(0));

    checkMediaDataInitial(result.getMediaMetadata());
  }

  @Test
  public void readWithDoublePropertyOnTeam() throws Exception {
    //The file contains the name property two times
    try {
      prepareAndExecuteEntry(INVALID_ENTRY_TEAM_DOUBLE_NAME_PROPERTY, "Teams", DEFAULT_PROPERTIES);
      fail("Exception has to be thrown");
    } catch (EntityProviderException e) {
      assertEquals(EntityProviderException.DOUBLE_PROPERTY.getKey(), e.getMessageReference().getKey());
    }
  }

  @Test
  public void entryWithMetadataElementProperties() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream(
        "{\"__metadata\":{\"properties\":{\"nt_Employees\":{\"associationuri\":"
        + "\"http://some.host.com/service.root/Teams('1')/$links/nt_Employees\"}}}," 
    		+ "\"Id\":\"1\"}");
    ODataEntry result = new JsonEntityConsumer().readEntry(entitySet, contentBody, DEFAULT_PROPERTIES);
    checkMediaDataInitial(result.getMediaMetadata());
  }

  private void checkMediaDataInitial(final MediaMetadata mediaMetadata) {
    assertNull(mediaMetadata.getContentType());
    assertNull(mediaMetadata.getEditLink());
    assertNull(mediaMetadata.getEtag());
    assertNull(mediaMetadata.getSourceLink());
  }

  @Test(expected = EntityProviderException.class)
  public void emptyEntry() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    new JsonEntityConsumer().readEntry(entitySet, createContentAsStream("{}"), DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongStart() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream(negativeJsonStart_1);
    new JsonEntityConsumer().readEntry(entitySet, contentBody, DEFAULT_PROPERTIES);
  }

  @Test(expected = EntityProviderException.class)
  public void wrongStart2() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream contentBody = createContentAsStream(negativeJsonStart_2);
    new JsonEntityConsumer().readEntry(entitySet, contentBody, DEFAULT_PROPERTIES);
  }
}
