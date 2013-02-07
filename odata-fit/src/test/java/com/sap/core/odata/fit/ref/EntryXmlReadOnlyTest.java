package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

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
    final String body = getBody(response);
    assertXpathEvaluatesTo("W/\"1\"", "/atom:entry/@m:etag", body);
    assertXpathEvaluatesTo("1", "/atom:entry/atom:content/m:properties/d:Id", body);

    response = callUri("Rooms('1')?$expand=nr_Employees");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    // assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertFalse(getBody(response).isEmpty());
    // assertTrue(getBody(response).contains(EMPLOYEE_1_NAME));

    response = callUri("Container2.Photos(Id=1,Type='image%2Fpng')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo("image/png", "/atom:entry/m:properties/d:Type", getBody(response));

    response = callUri("Container2.Photos(Id=4,Type='foo')");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertXpathEvaluatesTo("Container2.Photos(Id=4,Type='foo')/$value", "/atom:entry/atom:content/@src", getBody(response));

    notFound("Managers('5')");
    badRequest("Managers('3')?$top=1");
    badRequest("Rooms(X'33')");
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
