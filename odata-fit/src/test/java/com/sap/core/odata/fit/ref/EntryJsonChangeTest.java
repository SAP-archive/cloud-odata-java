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
package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

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
        + "\"Age\":52,\"EntryDate\":null,\"ImageUrl\":\"http://some.host:80/image.url\"}";
    putUri("Employees('2')", requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("Mister X", getBody(callUri("Employees('2')/EmployeeName/$value")));
  }
}
