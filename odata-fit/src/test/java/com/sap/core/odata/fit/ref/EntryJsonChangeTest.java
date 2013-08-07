package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;

/**
 * Tests employing the reference scenario changing entities in JSON format.
 * @author SAP AG
 */
public class EntryJsonChangeTest extends AbstractRefTest {

  @Test
  public void createEntry() throws Exception {
    final String requestBody = "{\"Id\":\"99\",\"Name\":\"Building 4\",\"Image\":\"" + PHOTO_DEFAULT_IMAGE + "\","
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('101')\"}}}";
    final HttpResponse response = postUri("Buildings()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    assertEquals("{\"d\":{\"Image\":\"" + PHOTO_DEFAULT_IMAGE + "\"}}", getBody(callUri("Buildings('4')/Image?$format=json")));
    checkUri("Buildings('4')/nb_Rooms('101')?$format=json");

    postUri("Buildings()", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void createEntryMinimal() throws Exception {
    final String requestBody = "{\"Id\":\"99\"}";
    final HttpResponse response = postUri("Teams()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    checkUri("Teams('4')?$format=json");
  }

  @Test
  public void createEntryWithNavigation() throws Exception {
    final String requestBody = "{\"Id\":\"199\",\"Name\":\"Room 199\"}";
    final HttpResponse response = postUri("Buildings('1')/nb_Rooms()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    checkUri("Rooms('104')?$format=json");
    assertEquals("1", getBody(callUri("Rooms('104')/nr_Building/Id/$value")));
    checkUri("Buildings('1')/nb_Rooms('104')?$format=json");
  }

  @Test
  public void createEntryWithLink() throws Exception {
    final String requestBody = "{\"Id\":\"99\",\"Name\":\"new room\",\"Seats\":19,\"Version\":42,"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('1')\"}}}";
    final HttpResponse response = postUri("Rooms()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    checkUri("Rooms('104')/nr_Building?$format=json");
    assertEquals("{\"d\":{\"Name\":\"Building 1\"}}", getBody(callUri("Rooms('104')/nr_Building/Name?$format=json")));
  }

  @Test
  public void createEntryWithInlineEntry() throws Exception {
    final String requestBody = "{\"Id\":\"99\",\"Name\":\"new room\",\"Seats\":19,\"Version\":42,"
        + "\"nr_Building\":{\"Id\":\"9\",\"Name\":\"new building\"}}";
    final HttpResponse response = postUri("Rooms()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    checkUri("Rooms('104')/nr_Building?$format=json");
    assertEquals("{\"d\":{\"Name\":\"new building\"}}", getBody(callUri("Rooms('104')/nr_Building/Name?$format=json")));
  }

  @Test
  public void createEntryWithInlineFeed() throws Exception {
    final String requestBody = "{\"Id\":\"99\",\"Name\":\"Building 4\",\"Image\":\"\","
        + "\"nb_Rooms\":[{\"Id\":\"201\",\"Name\":\"Room 201\",\"Seats\":9,\"Version\":2},"
        + "              {\"Id\":\"202\",\"Name\":\"Room 202\",\"Seats\":6,\"Version\":3,"
        + "               \"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Employees('5')\"}},"
        + "               \"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Employees('6')\"}}}]}";
    final HttpResponse response = postUri("Buildings()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    checkUri("Buildings('4')?$format=json");
    checkUri("Buildings('4')/nb_Rooms('104')?$format=json");
    assertEquals("{\"d\":{\"results\":[]}}", getBody(callUri("Buildings('4')/nb_Rooms('104')/nr_Employees?$format=json")));
    assertEquals("{\"d\":{\"Seats\":6}}", getBody(callUri("Buildings('4')/nb_Rooms('105')/Seats?$format=json")));
    assertEquals("{\"d\":{\"EmployeeName\":\"" + EMPLOYEE_5_NAME + "\"}}", getBody(callUri("Buildings('4')/nb_Rooms('105')/nr_Employees('5')/EmployeeName?$format=json")));
    assertEquals("{\"d\":{\"Age\":" + EMPLOYEE_6_AGE + "}}", getBody(callUri("Buildings('4')/nb_Rooms('105')/nr_Employees('6')/Age?$format=json")));
  }

  @Test
  public void createEntryThreeLevels() throws Exception {
    final String requestBody = "{\"Id\":\"99\",\"Name\":\"Building 4\",\"Image\":null,"
        + "\"nb_Rooms\":[{\"Id\":\"201\",\"Name\":\"Room 201\",\"Seats\":1,\"Version\":1,"
        + "\"nr_Employees\":[{\"EmployeeId\":\"99\",\"EmployeeName\":\"Ms X\",\"Age\":22,"
        + "\"Location\":{\"City\":{\"PostalCode\":null,\"CityName\":null},\"Country\":null},"
        + "\"EntryDate\":\"\\/Date(1424242424242)\\/\","
        + "\"ne_Manager\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Managers('1')\"}}}]}]}";
    final HttpResponse response = postUri("Buildings()", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    assertFalse(getBody(response).isEmpty());
    checkUri("Buildings('4')");
    assertEquals("1", getBody(callUri("Buildings('4')/nb_Rooms('104')/Seats/$value")));
    assertEquals("2015-02-18T06:53:44.242", getBody(callUri("Buildings('4')/nb_Rooms('104')/nr_Employees('7')/EntryDate/$value")));
    assertEquals(MANAGER_NAME, getBody(callUri("Buildings('4')/nb_Rooms('104')/nr_Employees('7')/ne_Manager/EmployeeName/$value")));
  }

  @Test
  public void updateEntry() throws Exception {
    final String requestBody = "{\"EmployeeId\":\"2\",\"EmployeeName\":\"Mister X\",\"ManagerId\":\"1\","
        + "\"RoomId\":\"1\",\"TeamId\":\"1\","
        + "\"Location\":{\"City\":{\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"},"
        + "              \"Country\":\"Germany\"},"
        + "\"EntryDate\":null,\"ImageUrl\":\"http://some.host:80/image.url\"}";
    putUri("Employees('2')", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("Mister X", getBody(callUri("Employees('2')/EmployeeName/$value")));
    assertEquals("0", getBody(callUri("Employees('2')/Age/$value")));
    assertEquals("{\"d\":{\"EntryDate\":null}}", getBody(callUri("Employees('2')/EntryDate?$format=json")));
  }

  @Test
  public void patchEntry() throws Exception {
    final String requestBody = "{\"Location\":"
        + "{\"City\":{\"PostalCode\":\"69124\",\"CityName\":\"" + CITY_1_NAME + "\"},"
        + " \"Country\":\"Germany\"},\"EntryDate\":null}";
    callUri(ODataHttpMethod.PATCH, "Employees('2')", null, null, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals(CITY_1_NAME, getBody(callUri("Employees('2')/Location/City/CityName/$value")));
    assertEquals("{\"d\":{\"EntryDate\":null}}", getBody(callUri("Employees('2')/EntryDate?$format=json")));
    assertEquals(EMPLOYEE_2_NAME, getBody(callUri("Employees('2')/EmployeeName/$value")));
    assertEquals(EMPLOYEE_2_AGE, getBody(callUri("Employees('2')/Age/$value")));
  }
}
