/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;

/**
 * @author SAP AG
 */
public class JsonEntryConsumerTest extends AbstractConsumerTest {

  //EntitySetConstants
  private static final String SIMPLEENTRYBUILDING = "JsonBuilding";
  private static final String SIMPLEENTRYEMPLOYEE = "JsonEmployee";
  private static final String SIMPLEENTRYTEAM = "JsonTeam";
  private static final String INVALIDENTRYTEAMDOUBLENAMEPROPERTY = "JsonInvalidTeamDoubleNameProperty";

  //Negative Test jsonStart
  public static final String negativeJsonStart_1 = "{ \"abc\": {";

  public static final String negativeJsonStart_2 = "{ \"d\": [a: 1, b: 2] }";

  @SuppressWarnings("unchecked")
  @Test
  public void readSimpleEmployeeEntry() throws Exception {
    ODataEntry result = prepareAndExecuteEntry(SIMPLEENTRYEMPLOYEE, "Employees", DEFAULT_PROPERTIES);

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
    ODataEntry result = prepareAndExecuteEntry(SIMPLEENTRYTEAM, "Teams", DEFAULT_PROPERTIES);

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
    ODataEntry result = prepareAndExecuteEntry(SIMPLEENTRYBUILDING, "Buildings", DEFAULT_PROPERTIES);
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
      prepareAndExecuteEntry(INVALIDENTRYTEAMDOUBLENAMEPROPERTY, "Teams", DEFAULT_PROPERTIES);
      fail("Exception has to be thrown");
    } catch (EntityProviderException e) {
      assertEquals(EntityProviderException.DOUBLE_PROPERTY.getKey(), e.getMessageReference().getKey());
    }
  }


  private void checkMediaDataInitial(final MediaMetadata mediaMetadata) {
    assertNull(mediaMetadata.getContentType());
    assertNull(mediaMetadata.getEditLink());
    assertNull(mediaMetadata.getEtag());
    assertNull(mediaMetadata.getSourceLink());
  }
}
