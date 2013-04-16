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

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario changing entities in XML format.
 * @author SAP AG
 */
public class EntryXmlCreateTest extends AbstractRefXmlTest {

  @Test
  public void createDeepInsertRoomWithFourEmployees() throws Exception {

    // prepare
    String content = readFile("room_w_four_inlined_employees.xml");
    assertNotNull(content);

    HttpResponse response = postUri("Rooms", content, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");

    assertEquals(getEndpoint() + "Rooms('104')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    checkEtag(response, "W/\"2\"");
    String body = getBody(response);
    assertXpathEvaluatesTo("5", "/atom:entry/atom:content/m:properties/d:Seats", body);
    assertXpathEvaluatesTo("2", "/atom:entry/atom:content/m:properties/d:Version", body);
    assertXpathEvaluatesTo("104", "/atom:entry/atom:content/m:properties/d:Id", body);
    assertXpathEvaluatesTo("Room 2", "/atom:entry/atom:content/m:properties/d:Name", body);
    assertXpathEvaluatesTo("7", "/atom:entry/atom:link/m:inline/atom:feed/atom:entry/m:properties/d:EmployeeId", body);
    //
    checkUri("Rooms('104')/nr_Employees('7')");
    checkUri("Employees('7')/ne_Room");
    response = callUri("Employees('7')/ne_Room/Seats/$value");
    body = getBody(response);
    assertEquals("5", body);

    checkUri("Rooms('104')/nr_Employees('8')");
    checkUri("Employees('7')/ne_Room");
    response = callUri("Employees('7')/ne_Room/Id/$value");
    body = getBody(response);
    assertEquals("104", body);

    checkUri("Rooms('104')/nr_Employees('9')");
    checkUri("Employees('7')/ne_Room");
    response = callUri("Employees('7')/ne_Room/Name/$value");
    body = getBody(response);
    assertEquals("Room 2", body);

    checkUri("Rooms('104')/nr_Employees('10')");
    checkUri("Employees('7')/ne_Room");
    response = callUri("Employees('7')/ne_Room/Version/$value");
    body = getBody(response);
    assertEquals("2", body);
  }
}
