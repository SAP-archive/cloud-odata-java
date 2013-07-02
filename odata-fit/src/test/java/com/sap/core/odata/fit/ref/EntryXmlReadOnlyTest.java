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
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.model.Photo;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;

/**
 * Tests employing the reference scenario reading a single entity in XML format.
 * @author SAP AG
 */
public class EntryXmlReadOnlyTest extends AbstractRefXmlTest {

  @Test
  public void entry() throws Exception {
    HttpResponse response = callUri("Employees('2')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertXpathEvaluatesTo(EMPLOYEE_2_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Managers('3')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo(EMPLOYEE_3_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Teams('2')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo("2", "/atom:entry/atom:content/m:properties/d:Id", getBody(response));

    response = callUri("Rooms('1')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    checkEtag(response, "W/\"1\"");
    String body = getBody(response);
    assertXpathEvaluatesTo("W/\"1\"", "/atom:entry/@m:etag", body);
    assertXpathEvaluatesTo("1", "/atom:entry/atom:content/m:properties/d:Id", body);

    response = callUri("Rooms('1')?$expand=nr_Employees");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    // assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertXpathEvaluatesTo(EMPLOYEE_1_NAME, "/atom:entry/atom:link[@href=\"Rooms('1')/nr_Employees\"]/m:inline/atom:feed/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Container2.Photos(Id=1,Type='image%2Fpng')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("image/png", "/atom:entry/m:properties/d:Type", body);
    assertXpathEvaluatesTo(IMAGE_JPEG, "/atom:entry/m:properties/d:Image/@m:MimeType", body);
    assertXpathEvaluatesTo(PHOTO_DEFAULT_IMAGE, "/atom:entry/m:properties/d:Image", body);

    response = callUri("Container2.Photos(Id=4,Type='foo')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo("Container2.Photos(Id=4,Type='foo')/$value", "/atom:entry/atom:content/@src", getBody(response));

    notFound("Managers('5')");
    notFound("Managers('1%2C2')");
    badRequest("Managers('3')?$top=1");
    badRequest("Rooms(X'33')");
  }

  @Test
  public void entryWithSpecialKey() throws Exception {
    // Ugly hack to create an entity with a key containing special characters just for this test.
    ListsProcessor processor = (ListsProcessor) getService().getEntityProcessor();
    Field field = processor.getClass().getDeclaredField("dataSource");
    field.setAccessible(true);
    ScenarioDataSource dataSource = (ScenarioDataSource) field.get(processor);
    field = dataSource.getClass().getDeclaredField("dataContainer");
    field.setAccessible(true);
    DataContainer dataContainer = (DataContainer) field.get(dataSource);
    // Add a new Photo where the "Type" property is set to, space-separated,
    // a literal percent character, all gen-delims, and all sub-delims
    // as specified by RFC 3986.
    dataContainer.getPhotos().add(new Photo(42, "strange Photo", "% :/?#[]@ !$&'()*+,;="));

    // Check that percent-decoding and -encoding works as expected.
    final String url = "Container2.Photos(Id=42,Type='%25%20%3A%2F%3F%23%5B%5D%40%20%21%24%26%27%28%29%2A%2B%2C%3B%3D')";
    final String expected = url.replace("%27", "''");
    final HttpResponse response = callUri(url);
    final String body = getBody(response);
    assertXpathEvaluatesTo("strange Photo", "/atom:entry/m:properties/d:Name", body);
    assertXpathEvaluatesTo(expected, "/atom:entry/atom:link[@rel=\"edit\"]/@href", body);
    assertXpathEvaluatesTo(expected + "/$value", "/atom:entry/atom:link[@rel=\"edit-media\"]/@href", body);
  }

  @Test
  public void expand() throws Exception {
    HttpResponse response = callUri("Employees('5')?$expand=ne_Manager");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    String body = getBody(response);
    assertXpathEvaluatesTo(EMPLOYEE_5_NAME, "/atom:entry/m:properties/d:EmployeeName", body);
    assertXpathEvaluatesTo(EMPLOYEE_3_NAME, "/atom:entry/atom:link[@href=\"Employees('5')/ne_Manager\"]/m:inline/atom:entry/m:properties/d:EmployeeName", body);

    response = callUri("Rooms('3')?$expand=nr_Employees/ne_Manager");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("3", "/atom:entry/atom:content[@type=\"application/xml\"]/m:properties/d:Id", body);
    assertXpathEvaluatesTo("1", "count(/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry)", body);
    assertXpathEvaluatesTo(EMPLOYEE_5_NAME, "/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry/m:properties/d:EmployeeName", body);
    assertXpathEvaluatesTo(EMPLOYEE_3_NAME, "/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry/atom:link[@href=\"Employees('5')/ne_Manager\"]/m:inline/atom:entry/m:properties/d:EmployeeName", body);

    notFound("Employees('3')?$expand=noNavProp");
    badRequest("Employees('3')?$expand=Age");
    badRequest("Employees()?$expand=ne_Room/Seats");
    notFound("Employees()?$expand=ne_Room/noNavProp");
  }

  @Test
  public void select() throws Exception {
    HttpResponse response = callUri("Employees('6')?$select=EmployeeId,Age");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    String body = getBody(response);
    assertXpathEvaluatesTo("6", "/atom:entry/m:properties/d:EmployeeId", body);
    assertXpathEvaluatesTo(EMPLOYEE_6_AGE, "/atom:entry/m:properties/d:Age", body);
    assertXpathNotExists("/atom:entry/m:properties/d:Location", body);
    assertXpathEvaluatesTo("2", "count(/atom:entry/atom:link)", body);

    response = callUri("Employees('3')/ne_Room?$select=Seats");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("5", "/atom:entry/atom:content/m:properties/d:Seats", body);
    assertXpathNotExists("/atom:entry/m:properties/d:Id", body);

    final String entry = getBody(callUri("Employees('6')"));
    assertEquals(entry, getBody(callUri("Employees('6')?$select=*,Age")));
    assertEquals(entry, getBody(callUri("Employees('6')?$select=*,ne_Room")));

    checkUri("Container2.Photos(Id=4,Type='foo')?$select=%D0%A1%D0%BE%D0%B4%D0%B5%D1%80%D0%B6%D0%B0%D0%BD%D0%B8%D0%B5,Id");

    response = callUri("Employees('6')?$expand=ne_Room&$select=ne_Room/Version");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("2", "/atom:entry/atom:link[@href=\"Employees('6')/ne_Room\"]/m:inline/atom:entry/atom:content[@type=\"application/xml\"]/m:properties/d:Version", body);
    assertXpathNotExists("/atom:entry/m:properties/d:Location", body);
    assertXpathNotExists("/atom:entry/atom:link[@href=\"Employees('6')/ne_Room\"]/m:inline/atom:entry/atom:content[@type=\"application/xml\"]/m:properties/d:Seats", body);

    response = callUri("Rooms('3')?$expand=nr_Employees/ne_Team&$select=nr_Employees/ne_Team/Name");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("Team 2", "/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry/atom:link[@href=\"Employees('5')/ne_Team\"]/m:inline/atom:entry/atom:content/m:properties/d:Name", body);
    assertXpathNotExists("/atom:entry/atom:content/m:properties", body);
    assertXpathNotExists("/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry/m:properties", body);
    assertXpathNotExists("/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry/atom:link[@href=\"Employees('5')/ne_Team\"]/m:inline/atom:entry/atom:content/m:properties/d:Id", body);

    notFound("Teams('3')?$select=noProp");
    notFound("Teams()?$select=nt_Employees/noProp");
    notFound("Employees('3')/ne_Room?$select=Age");
    notFound("Employees('3')/ne_Room?$select=ne_Room");
  }

  @Test
  public void entryMediaResource() throws Exception {
    HttpResponse response = callUri("Employees('2')/$value");
    checkMediaType(response, IMAGE_JPEG);
    assertNotNull(getBody(response));

    response = callUri("Employees('2')/$value?$format=xml", HttpStatusCodes.BAD_REQUEST);
    String body = getBody(response);
    XMLAssert.assertXpathExists("/m:error/m:message", body);
  }

  @Test
  public void navigationEntry() throws Exception {
    HttpResponse response = callUri("Employees('2')/ne_Manager");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo(MANAGER_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Employees('2')/ne_Team/nt_Employees('1')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo(EMPLOYEE_1_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Employees('2')/ne_Manager/nm_Employees('6')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo(EMPLOYEE_6_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    badRequest("Employees('2')/ne_Manager()");
  }
}
