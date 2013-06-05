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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.google.gson.internal.StringMap;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class EntryJsonCreateInlineTest extends AbstractRefJsonTest {

  @Test
  public void createThreeLevelsDeepInsert() throws Exception {
    String content = "{\"Name\" : \"Room 2\",\"nr_Building\" : {\"Name\" : \"Building 2\",\"nb_Rooms\" : {\"results\" : [{"
        + "\"nr_Employees\" : {\"__deferred\" : {\"uri\" : \"" + getEndpoint() + "Rooms('1')/nr_Employees\""
        + "}},\"nr_Building\" : {\"__deferred\" : {\"uri\" : \"" + getEndpoint() + "/Rooms('1')/nr_Building\""
        + "}}}]}}}";

    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);

    String body = getBody(response);

    //Check inline building
    StringMap<?> map = getStringMap(body);
    map = (StringMap<?>) map.get("nr_Building");
    assertNotNull(map);
    assertEquals("Building 2", map.get("Name"));

    //Check inline rooms of the inline building
    map = (StringMap<?>) map.get("nb_Rooms");
    assertNotNull(map);

    ArrayList<?> results = (ArrayList<?>) map.get("results");
    assertNotNull(results);
    assertEquals(2, results.size());
  }

  @Test
  public void createEntryRoomWithInlineEntry() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\",\"Seats\":4,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 4\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('2')/nb_Rooms\"}}}}}";

    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    checkEtag(response, "W/\"2\"");

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("104", map.get("Id"));
    assertEquals("Room 104", map.get("Name"));

    @SuppressWarnings("unchecked")
    StringMap<String> metadataMap = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap);
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("id"));
    assertEquals("RefScenario.Room", metadataMap.get("type"));
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("uri"));

    response = callUri("Rooms('104')/nr_Building/", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    body = getBody(response);
    map = getStringMap(body);
    assertEquals("4", map.get("Id"));
    assertEquals("Building 4", map.get("Name"));

    @SuppressWarnings("unchecked")
    StringMap<String> metadataMap2 = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap2);
    assertEquals(getEndpoint() + "Buildings('4')", metadataMap2.get("id"));
    assertEquals("RefScenario.Building", metadataMap2.get("type"));
    assertEquals(getEndpoint() + "Buildings('4')", metadataMap2.get("uri"));
  }

  @Test
  public void createEntryRoomWithInlineEmptyFeedArray() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\",\"Seats\":4,\"Version\":2,"
        + "\"nr_Employees\":[],"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Building\"}}}}";
    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    checkEtag(response, "W/\"2\"");

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("104", map.get("Id"));
    assertEquals("Room 104", map.get("Name"));

    @SuppressWarnings("unchecked")
    StringMap<String> metadataMap = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap);
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("id"));
    assertEquals("RefScenario.Room", metadataMap.get("type"));
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("uri"));
  }

  @Test
  public void createEntryRoomWithInlineEmptyFeedObject() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\",\"Seats\":4,\"Version\":2,"
        + "\"nr_Employees\":{\"results\":[]},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Building\"}}}}";
    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    checkEtag(response, "W/\"2\"");

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("104", map.get("Id"));
    assertEquals("Room 104", map.get("Name"));

    @SuppressWarnings("unchecked")
    StringMap<String> metadataMap = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap);
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("id"));
    assertEquals("RefScenario.Room", metadataMap.get("type"));
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("uri"));
  }

  @Test
  public void createEntryRoomWithInlineFeedArray() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":[{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('2')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('2')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"2\\\"\"},"
        + "\"Id\":\"2\",\"Name\":\"Room 2\",\"Seats\":5,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Building\"}}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"3\",\"Name\":\"Room 3\",\"Seats\":2,\"Version\":3,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Building\"}}}]}}";

    HttpResponse response = postUri("Buildings", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    //checkEtag(response, "W/\"2\"");

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("4", map.get("Id"));
    assertEquals("Building 2", map.get("Name"));

    @SuppressWarnings("unchecked")
    StringMap<String> metadataMap = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap);
    assertEquals(getEndpoint() + "Buildings('4')", metadataMap.get("id"));
    assertEquals("RefScenario.Building", metadataMap.get("type"));
    assertEquals(getEndpoint() + "Buildings('4')", metadataMap.get("uri"));

    response = callUri("Buildings('4')/nb_Rooms('104')/", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    body = getBody(response);
    map = getStringMap(body);
    assertEquals("104", map.get("Id"));
    assertEquals("Room 2", map.get("Name"));
    response = callUri("Buildings('4')/nb_Rooms('104')/Seats/$value", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    body = getBody(response);
    assertEquals("5", body);

    response = callUri("Buildings('4')/nb_Rooms('105')/", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    body = getBody(response);
    map = getStringMap(body);
    assertEquals("105", map.get("Id"));
    assertEquals("Room 3", map.get("Name"));
    response = callUri("Buildings('4')/nb_Rooms('105')/Seats/$value", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    body = getBody(response);
    assertEquals("2", body);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void createEntryRoomWithInlineFeedObject() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":{\"results\":[{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('2')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('2')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"2\\\"\"},"
        + "\"Id\":\"2\",\"Name\":\"Room 2\",\"Seats\":5,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Building\"}}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"3\",\"Name\":\"Room 3\",\"Seats\":2,\"Version\":3,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Building\"}}}]}}}";

    HttpResponse response = callUri("Buildings('4')/nb_Rooms('104')/", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NOT_FOUND);
    getBody(response);
    response = callUri("Buildings('4')/nb_Rooms('105')/", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NOT_FOUND);
    getBody(response);

    response = postUri("Buildings", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("4", map.get("Id"));
    assertEquals("Building 2", map.get("Name"));

    StringMap<String> metadataMap = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap);
    assertEquals(getEndpoint() + "Buildings('4')", metadataMap.get("id"));
    assertEquals("RefScenario.Building", metadataMap.get("type"));
    assertEquals(getEndpoint() + "Buildings('4')", metadataMap.get("uri"));

    StringMap<Object> navProperty = (StringMap<Object>) map.get("nb_Rooms");
    assertNotNull(navProperty);
    List<StringMap<String>> results = (ArrayList<StringMap<String>>) navProperty.get("results");
    assertNotNull(results);
    for (int i = 0; i < results.size(); i++) {
      StringMap<String> resultMap = results.get(i);
      switch (i) {
      case 0:
        assertEquals("Room 2", resultMap.get("Name"));
        assertEquals("104", resultMap.get("Id"));
        break;
      case 1:
        assertEquals("105", resultMap.get("Id"));
        assertEquals("Room 3", resultMap.get("Name"));
      }
    }
    response = callUri("Buildings('4')/nb_Rooms('104')/Seats/$value", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    assertEquals("5", getBody(response));

    response = callUri("Buildings('4')/nb_Rooms('105')/Seats/$value", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    assertEquals("2", getBody(response));
  }

  @Test
  public void createEntryRoomWithInlineFeedEmployee() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\",\"Seats\":4,\"Version\":2,"
        + "\"nr_Employees\":{\"results\":[{"
        + "\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Employees('1')\","
        + "\"type\":\"RefScenario.Employee\","
        + "\"content_type\":\"image/jpeg\","
        + "\"media_src\":\"" + getEndpoint() + "Employees('1')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Employees('1')/$value\"},"
        + " \"EmployeeName\": \"Walter Winter\","
        + "\"ManagerId\": \"1\","
        + "\"RoomId\": \"1\","
        + "\"TeamId\": \"1\","
        + "\"Age\": 52,"
        + "\"ImageUrl\": \"" + getEndpoint() + "Employees('1')/$value\"}]},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Building\"}}}}";

    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("104", map.get("Id"));
    assertEquals("Room 104", map.get("Name"));

    @SuppressWarnings("unchecked")
    StringMap<String> metadataMap = (StringMap<String>) map.get("__metadata");
    assertNotNull(metadataMap);
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("id"));
    assertEquals("RefScenario.Room", metadataMap.get("type"));
    assertEquals(getEndpoint() + "Rooms('104')", metadataMap.get("uri"));
  }

  @Test
  public void createEntryRoomWithInvalidFeed() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\",\"Seats\":4,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"results\":[]}}}";
    postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void createEntryRoomInlineFeedWithInvalidProperty() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":{\"results\":[{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('2')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('2')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"2\\\"\"},"
        + "\"Id\":\"2\",\"Name\":\"Room 2\",\"Seats\":5,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Building\"}}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"3\",\"Name\":\"Room 3\",\"Seats\":2,\"Version\":3,\"Id\":\"2\""
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Building\"}}}]}}}";

    postUri("Buildings", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.BAD_REQUEST);
  }

}
