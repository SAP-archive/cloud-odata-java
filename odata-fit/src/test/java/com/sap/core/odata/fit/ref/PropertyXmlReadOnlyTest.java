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
import static org.junit.Assert.assertNull;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;

/**
 * Tests employing the reference scenario reading properties in XML format.
 * @author SAP AG
 */
public class PropertyXmlReadOnlyTest extends AbstractRefXmlTest {
  @Test
  public void simpleProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/Age/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN_UTF8);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertEquals(EMPLOYEE_2_AGE, getBody(response));

    response = callUri("Employees('2')/Age");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertXpathEvaluatesTo(EMPLOYEE_2_AGE, "/d:Age", getBody(response));

    response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/Image/$value");
    checkMediaType(response, IMAGE_JPEG);
    checkEtag(response, "W/\"3\"");
    assertNotNull(getBody(response));

    response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/Image");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(IMAGE_JPEG, "/d:Image/@m:MimeType", getBody(response));

    response = callUri("Rooms('2')/Seats/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN_UTF8);
    checkEtag(response, "W/\"2\"");
    assertEquals("5", getBody(response));

    response = callUri("Rooms('2')/Seats");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    checkEtag(response, "W/\"2\"");
    assertXpathEvaluatesTo("5", "/d:Seats", getBody(response));

    response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/BinaryData/$value");
    checkMediaType(response, IMAGE_JPEG);
    assertNotNull(getBody(response));

    response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/BinaryData");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(IMAGE_JPEG, "/d:BinaryData/@m:MimeType", getBody(response));

    notFound("Employees('2')/Foo");
    badRequest("Employees('2')/Age()");
  }

  @Test
  public void navigationSimpleProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/ne_Room/nr_Employees('6')/Age");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(EMPLOYEE_6_AGE, "/d:Age", getBody(response));

    response = callUri("Employees('4')/ne_Team/nt_Employees('5')/EmployeeName");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(EMPLOYEE_5_NAME, "/d:EmployeeName", getBody(response));

    response = callUri("Rooms('2')/nr_Employees('4')/Location/City/CityName");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(CITY_2_NAME, "/d:CityName", getBody(response));
  }

  @Test
  public void complexProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/Location/City/CityName/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN_UTF8);
    assertEquals(CITY_2_NAME, getBody(response));

    response = callUri("Employees('2')/Location");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(CITY_2_NAME, "/d:Location/d:City/d:CityName", getBody(response));

    badRequest("Employees('2')/Location()");
    notFound("Employees('2')/Location/City/$value");
  }
}
