/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonEntityConsumerTest extends AbstractConsumerTest {

  //EntitySetConstants
  private static final int SIMPLEENTRYBUILDING = 0;
  private static final int SIMPLEENTRYEMPLOYEE = 1;
  private static final int SIMPLEENTRYTEAM = 2;
  private static final int INVALIDENTRYTEAMDOUBLENAMEPROPERTY = 3;

  //Negative Test jsonStart
  public static final String negativeJsonStart_1 = "{ \"abc\": {";

  public static final String negativeJsonStart_2 = "{ \"d\": [a: 1, b: 2] }";

  @SuppressWarnings("unchecked")
  @Test
  public void readSimpleEmployeeEntry() throws Exception {
    ODataEntry result = prepareAndExecute(SIMPLEENTRYEMPLOYEE);

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
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')/ne_Manager", associationUris.get(0));

    associationUris = result.getMetadata().getAssociationUris("ne_Team");
    assertEquals(1, associationUris.size());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')/ne_Team", associationUris.get(0));

    associationUris = result.getMetadata().getAssociationUris("ne_Room");
    assertEquals(1, associationUris.size());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')/ne_Room", associationUris.get(0));

    MediaMetadata mediaMetadata = result.getMediaMetadata();
    assertEquals("image/jpeg", mediaMetadata.getContentType());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')/$value", mediaMetadata.getEditLink());
    assertEquals("Employees('1')/$value", mediaMetadata.getSourceLink());
    assertNull(mediaMetadata.getEtag());
  }

  @Test
  public void readSimpleTeamEntry() throws Exception {
    ODataEntry result = prepareAndExecute(SIMPLEENTRYTEAM);

    Map<String, Object> properties = result.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
    assertNull(properties.get("nt_Employees"));

    List<String> associationUris = result.getMetadata().getAssociationUris("nt_Employees");
    assertEquals(1, associationUris.size());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Teams('1')/nt_Employees", associationUris.get(0));

    checkMediaDataInitial(result.getMediaMetadata());
  }

  @Test
  public void readSimpleBuildingEntry() throws Exception {
    ODataEntry result = prepareAndExecute(SIMPLEENTRYBUILDING);
    //verify
    Map<String, Object> properties = result.getProperties();
    assertNotNull(properties);
    assertEquals("1", properties.get("Id"));
    assertEquals("Building 1", properties.get("Name"));
    assertEquals(null, properties.get("Image"));
    assertNull(properties.get("nb_Rooms"));

    List<String> associationUris = result.getMetadata().getAssociationUris("nb_Rooms");
    assertEquals(1, associationUris.size());
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Buildings('1')/nb_Rooms", associationUris.get(0));

    checkMediaDataInitial(result.getMediaMetadata());
  }

  @Test
  public void readWithDoublePropertyOnTeam() throws Exception {
    //The file contains the name property two times
    try {
      prepareAndExecute(INVALIDENTRYTEAMDOUBLENAMEPROPERTY);      
    } catch (EntityProviderException e) {
      assertEquals(EntityProviderException.DOUBLE_PROPERTY.getKey(), e.getMessageReference().getKey());
    }    
  }

  private ODataEntry prepareAndExecute(int entryIdentificator) throws IOException, EdmException, ODataException, UnsupportedEncodingException, EntityProviderException {
    EdmEntitySet entitySet = null;
    String content = null;
    switch (entryIdentificator) {
    case SIMPLEENTRYBUILDING:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Buildings");
      content = readFile("JsonBuilding");
      break;
    case SIMPLEENTRYTEAM:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
      content = readFile("JsonTeam");
      break;
    case SIMPLEENTRYEMPLOYEE:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
      content = readFile("JsonEmployee");
      break;
    case INVALIDENTRYTEAMDOUBLENAMEPROPERTY:
      entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
      content = readFile("JsonInvalidTeamDoubleNameProperty");
      break;
    default:
      break;
    }

    assertNotNull(content);
    InputStream contentBody = createContentAsStream(content);

    // execute
    JsonEntityConsumer xec = new JsonEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
    return result;
  }  
  
  private void checkMediaDataInitial(MediaMetadata mediaMetadata) {
    assertNull(mediaMetadata.getContentType());
    assertNull(mediaMetadata.getEditLink());
    assertNull(mediaMetadata.getEtag());
    assertNull(mediaMetadata.getSourceLink());
  }
}
