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
import static org.junit.Assert.assertNull;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.google.gson.internal.StringMap;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * @author SAP AG
 */
public class EntryJsonCreateTest extends AbstractRefJsonTest {

  @Test
  public void createEntryRoom() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\",\"Seats\":4,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Building\"}}}}";
    assertNotNull(content);
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

    response = callUri("Rooms('104')/Seats/$value");
    body = getBody(response);
    assertEquals("4", body);
  }

  @Test
  public void createEntryRoomWithLink() throws Exception {
    String content = "{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 104\","
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Building\"}}}}";
    assertNotNull(content);
    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_JSON, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("104", map.get("Id"));
    assertEquals("Room 104", map.get("Name"));
    @SuppressWarnings("unchecked")
    StringMap<Object> employeesMap = (StringMap<Object>) map.get("nr_Employees");
    assertNotNull(employeesMap);
    @SuppressWarnings("unchecked")
    StringMap<String> deferredMap = (StringMap<String>) employeesMap.get("__deferred");
    assertNotNull(deferredMap);
    assertEquals(getEndpoint() + "Rooms('104')/nr_Employees", deferredMap.get("uri"));
  }

  @Test
  public void createEntryEmployee() throws Exception {
    String content = "{iVBORw0KGgoAAAANSUhEUgAAAB4AAAAwCAIAAACJ9F2zAAAAA}";

    assertNotNull(content);
    HttpResponse response = postUri("Employees", content, HttpContentType.TEXT_PLAIN, HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);

    String body = getBody(response);
    StringMap<?> map = getStringMap(body);
    assertEquals("7", map.get("EmployeeId"));
    assertEquals("Employee 7", map.get("EmployeeName"));
    assertNull(map.get("EntryData"));
    response = callUri("Employees('7')/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN);
  }

}
