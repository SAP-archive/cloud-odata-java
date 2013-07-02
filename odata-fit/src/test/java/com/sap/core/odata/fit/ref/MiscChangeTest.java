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

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;

/**
 * Tests employing the reference scenario that use neither XML nor JSON
 * and that change data in some way
 * @author SAP AG
 */
public class MiscChangeTest extends AbstractRefTest {

  @Test
  public void deleteEntry() throws Exception {
    deleteUriOk("Employees('2')");
    deleteUriOk("Managers('3')");
    deleteUriOk("Teams('2')");
    callUri(ODataHttpMethod.DELETE, "Rooms('1')", HttpHeaders.IF_MATCH, "W/\"1\"", null, null, HttpStatusCodes.NO_CONTENT);
    callUri(ODataHttpMethod.DELETE, "Container2.Photos(Id=1,Type='image%2Fpng')",
        HttpHeaders.IF_MATCH, "W/\"1\"", null, null, HttpStatusCodes.NO_CONTENT);

    // deleteUri("Rooms('1')", HttpStatusCodes.PRECONDITION_REQUIRED);
    deleteUri("Managers()", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Managers('5')", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/ne_Manager", HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void deletePropertyValue() throws Exception {
    deleteUriOk("Employees('2')/Age/$value");
    deleteUriOk("Employees('2')/Location/City/PostalCode/$value");

    deleteUri("Employees('2')/Age", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Employees('2')/Foo/$value", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/EmployeeId/$value", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Employees('2')/Location/City/$value", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/ne_Manager/Age/$value", HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void deleteLink() throws Exception {
    deleteUriOk("Employees('6')/$links/ne_Room");
    deleteUriOk("Managers('3')/$links/nm_Employees('5')");

    deleteUri("Managers('3')/$links/nm_Employees()", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Managers('3')/$links/nm_Employees('1')", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/ne_Team/$links/nt_Employees('1')", HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void deleteMediaResource() throws Exception {
    deleteUriOk("Managers('1')/$value");

    deleteUri("Teams('2')/$value", HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void updateMediaResource() throws Exception {
    final String url = "Managers('1')/$value";
    putUri(url, "00", HttpContentType.APPLICATION_OCTET_STREAM, HttpStatusCodes.NO_CONTENT);
    HttpResponse response = callUri(url);
    checkMediaType(response, HttpContentType.APPLICATION_OCTET_STREAM);
    assertEquals("00", getBody(response));

    response = callUri(ODataHttpMethod.PUT, "Container2.Photos(Id=2,Type='image%2Fbmp')/$value", null, null, "00", IMAGE_GIF, HttpStatusCodes.NO_CONTENT);
    checkEtag(response, "W/\"2\"");
  }

  @Test
  public void updatePropertyValue() throws Exception {
    putUri("Employees('2')/Age/$value", "42", HttpContentType.TEXT_PLAIN, HttpStatusCodes.NO_CONTENT);

    String url = "Container2.Photos(Id=3,Type='image%2Fjpeg')/Image/$value";
    callUri(ODataHttpMethod.PUT, url, HttpHeaders.ETAG, "W/\"3\"", "4711", HttpContentType.APPLICATION_OCTET_STREAM, HttpStatusCodes.NO_CONTENT);
    assertEquals("4711", getBody(callUri(url)));

    url = "Container2.Photos(Id=4,Type='foo')/BinaryData/$value";
    HttpResponse response = callUri(ODataHttpMethod.PUT, url, HttpHeaders.ETAG, "W/\"4\"", "4711", IMAGE_JPEG, HttpStatusCodes.NO_CONTENT);
    checkEtag(response, "W/\"4\"");
    assertEquals("4711", getBody(callUri(url)));

    final String content = "2012-02-29T00:00:00";
    url = "Employees('2')/EntryDate/$value";
    putUri(url, content, HttpContentType.TEXT_PLAIN, HttpStatusCodes.NO_CONTENT);
    assertEquals(content, getBody(callUri(url)));

    putUri("Employees('2')/EmployeeId/$value", "42", HttpContentType.TEXT_PLAIN, HttpStatusCodes.METHOD_NOT_ALLOWED);
    putUri("Employees('2')/Age/$value", "42a", HttpContentType.TEXT_PLAIN, HttpStatusCodes.BAD_REQUEST);
    putUri(url, "2000-13-78T42:19:18z", HttpContentType.TEXT_PLAIN, HttpStatusCodes.BAD_REQUEST);
  }
}
