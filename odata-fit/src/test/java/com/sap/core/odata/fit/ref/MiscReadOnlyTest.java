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

/**
 * Read-only tests employing the reference scenario that use neither XML nor JSON
 * @author SAP AG
 */
public class MiscReadOnlyTest extends AbstractRefTest {

  @Test
  public void checkUrls() throws Exception {
    checkUri("/");

    checkUri("Managers('1')/$links/nm_Employees");
    checkUri("Managers('1')/$links/nm_Employees()");
    checkUri("Managers('1')/$links/nm_Employees('2')");
    checkUri("Employees('1')/ne_Room/nr_Employees");
    checkUri("Employees('1')/ne_Room/nr_Employees()");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')");

    checkUri("Employees('2')/ne_Team/nt_Employees('1')/Location");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/Location/City/CityName/$value");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/$links/ne_Room");
    checkUri("Employees('2')/ne_Team/nt_Employees('1')/ne_Room/$links/nr_Employees");

    checkUri("Employees('2')/ne_Team/nt_Employees('3')/ne_Room");
    checkUri("Employees('2')/ne_Team/nt_Employees('3')/ne_Room/nr_Employees");
    checkUri("Employees('2')/ne_Manager");
    checkUri("Employees('2')/ne_Manager/$links/nm_Employees()");
    checkUri("Employees('2')/ne_Manager/nm_Employees('3')");
    checkUri("Employees('2')/ne_Manager/nm_Employees('3')/Age");
  }

  @Test
  public void count() throws Exception {
    assertEquals("103", getBody(callUri("Rooms()/$count")));
    assertEquals("4", getBody(callUri("Rooms('2')/nr_Employees/$count")));
    assertEquals("1", getBody(callUri("Employees('1')/ne_Room/$count")));
    assertEquals("1", getBody(callUri("Managers('3')/nm_Employees('5')/$count")));
    assertEquals("4", getBody(callUri("Rooms('2')/$links/nr_Employees/$count")));
    assertEquals("1", getBody(callUri("Employees('1')/$links/ne_Room/$count")));
    assertEquals("1", getBody(callUri("Managers('3')/$links/nm_Employees('5')/$count")));

    badRequest("Rooms('1')/Seats/$count");
    notFound("Managers('3')/nm_Employees('1')/$count");
  }

  @Test
  public void mediaResource() throws Exception {
    HttpResponse response = callUri("Employees('3')/$value");
    checkMediaType(response, IMAGE_JPEG);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertNotNull(getBody(response));

    response = callUri("Managers('1')/$value");
    checkMediaType(response, IMAGE_JPEG);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    final String expected = getBody(response);

    response = callUri("Employees('2')/ne_Manager/$value");
    checkMediaType(response, IMAGE_JPEG);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertEquals(expected, getBody(response));

    response = callUri("Container2.Photos(Id=1,Type='image%2Fpng')/$value");
    checkMediaType(response, IMAGE_JPEG);
    checkEtag(response, "W/\"1\"");
    assertNotNull(getBody(response));

    notFound("Employees('99')/$value");
    badRequest("Teams('3')/$value");
  }
}
