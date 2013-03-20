package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario reading a single entity in XML format
 * @author SAP AG
 */
public class EntryXmlReadOnlyTest extends AbstractRefXmlTest {
  @Test
  public void entry() throws Exception {
    HttpResponse response = callUri("Employees('2')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertXpathEvaluatesTo(EMPLOYEE_2_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Managers('3')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo(EMPLOYEE_3_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Teams('2')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo("2", "/atom:entry/atom:content/m:properties/d:Id", getBody(response));

    response = callUri("Rooms('1')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    checkEtag(response, "W/\"1\"");
    String body = getBody(response);
    assertXpathEvaluatesTo("W/\"1\"", "/atom:entry/@m:etag", body);
    assertXpathEvaluatesTo("1", "/atom:entry/atom:content/m:properties/d:Id", body);

    response = callUri("Rooms('1')?$expand=nr_Employees");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertNotNull(getBody(response));
    // assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    // assertXpathEvaluatesTo(EMPLOYEE_1_NAME, "/atom:entry/atom:link[@href=\"Rooms('1')/nr_Employees\"]/m:inline/atom:feed/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Container2.Photos(Id=1,Type='image%2Fpng')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("image/png", "/atom:entry/m:properties/d:Type", body);
    assertXpathEvaluatesTo(IMAGE_JPEG, "/atom:entry/m:properties/d:Image/@m:MimeType", body);

    response = callUri("Container2.Photos(Id=4,Type='foo')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo("Container2.Photos(Id=4,Type='foo')/$value", "/atom:entry/atom:content/@src", getBody(response));

    notFound("Managers('5')");
    badRequest("Managers('3')?$top=1");
    badRequest("Rooms(X'33')");
  }

  @Ignore("Expand under developement")
  @Test
  public void select() throws Exception {
    HttpResponse response = callUri("Employees('6')?$select=EmployeeId,Age");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    String body = getBody(response);
    assertXpathEvaluatesTo("6", "/atom:entry/m:properties/d:EmployeeId", body);
    assertXpathEvaluatesTo(EMPLOYEE_6_AGE, "/atom:entry/m:properties/d:Age", body);
    assertXpathNotExists("/atom:entry/m:properties/d:Location", body);
    assertXpathEvaluatesTo("2", "count(/atom:entry/atom:link)", body);

    response = callUri("Employees('3')/ne_Room?$select=Seats");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("5", "/atom:entry/atom:content/m:properties/d:Seats", body);
    assertXpathNotExists("/atom:entry/m:properties/d:Id", body);

    final String entry = getBody(callUri("Employees('6')"));
    assertEquals(entry, getBody(callUri("Employees('6')?$select=*,Age")));
    assertEquals(entry, getBody(callUri("Employees('6')?$select=*,ne_Room")));

    checkUri("Container2.Photos(Id=4,Type='foo')?$select=%D0%A1%D0%BE%D0%B4%D0%B5%D1%80%D0%B6%D0%B0%D0%BD%D0%B8%D0%B5,Id");

    response = callUri("Employees('6')?$expand=ne_Room&$select=ne_Room/Version");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    body = getBody(response);
    // assertXpathEvaluatesTo("2", "/atom:entry/atom:link[@href=\"Employees('6')/ne_Room\"]/m:inline/atom:entry/atom:content[@type=\"application/xml\"]/m:properties/d:Version", body);
    assertXpathNotExists("/atom:entry/m:properties/d:Location", body);
    assertXpathNotExists("/atom:entry/atom:link[@href=\"Employees('6')/ne_Room\"]/m:inline/atom:entry/atom:content[@type=\"application/xml\"]/m:properties/d:Seats", body);

    response = callUri("Rooms('3')?$expand=nr_Employees/ne_Team&$select=nr_Employees/ne_Team/Name");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    body = getBody(response);
    // assertXpathEvaluatesTo("Team 2", "/atom:entry/atom:link[@href=\"Rooms('3')/nr_Employees\"]/m:inline/atom:feed/atom:entry/atom:link[@href=\"Employees('5')/ne_Team\"]/m:inline/atom:entry/atom:content/m:properties/d:Name", body);
    // assertXpathNotExists("/atom:entry/atom:content/m:properties", body);
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
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo(MANAGER_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Employees('2')/ne_Team/nt_Employees('1')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo(EMPLOYEE_1_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("Employees('2')/ne_Manager/nm_Employees('6')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo(EMPLOYEE_6_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    badRequest("Employees('2')/ne_Manager()");
  }
}
