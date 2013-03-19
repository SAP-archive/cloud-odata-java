package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;

/**
 * Tests employing the reference scenario reading function-import output in XML format
 * @author SAP AG
 */
public class FunctionImportTest extends AbstractRefXmlTest {

  @Test
  public void functionImports() throws Exception {
    HttpResponse response = callUri("EmployeeSearch('1')/ne_Room/Id/$value?q='alter'");
    checkMediaType(response, HttpContentType.TEXT_PLAIN_UTF8);
    // checkEtag(response, "W/\"1\"");
    assertEquals("1", getBody(response));

    assertFalse(getBody(callUri("EmployeeSearch?q='-'")).contains("entry"));

    response = callUri("AllLocations");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathExists("/d:AllLocations/d:element/d:City[d:CityName=\"" + CITY_2_NAME + "\"]", getBody(response));

    response = callUri("AllUsedRoomIds");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathExists("/d:AllUsedRoomIds[d:element=\"3\"]", getBody(response));

    response = callUri("MaximalAge");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(EMPLOYEE_3_AGE, "/d:MaximalAge", getBody(response));

    response = callUri("MostCommonLocation");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(CITY_2_NAME, "/d:MostCommonLocation/d:City/d:CityName", getBody(response));

    checkUri("ManagerPhoto?Id='1'");

    response = callUri("ManagerPhoto/$value?Id='1'");
    checkMediaType(response, IMAGE_JPEG);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertNotNull(getBody(response));

    response = callUri("OldestEmployee", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_XML);
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(EMPLOYEE_3_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    response = callUri("OldestEmployee?$format=xml");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertXpathEvaluatesTo(EMPLOYEE_3_NAME, "/atom:entry/m:properties/d:EmployeeName", getBody(response));

    badRequest("AllLocations/$count");
    badRequest("AllUsedRoomIds/$value");
    badRequest("MaximalAge()");
    badRequest("MostCommonLocation/City/CityName");
    badRequest("ManagerPhoto");
    badRequest("OldestEmployee()");
    notFound("ManagerPhoto?Id='2'");
  }

  @Test
  public void select() throws Exception {
    HttpResponse response = callUri("EmployeeSearch?q='ede'&$select=Age");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=feed");
    String body = getBody(response);
    assertXpathEvaluatesTo(EMPLOYEE_2_AGE, "/atom:feed/atom:entry/m:properties/d:Age", body);
    assertXpathNotExists("/atom:feed/atom:entry/m:properties/d:Location", body);

    response = callUri("EmployeeSearch('2')/ne_Room?q='ede'&$select=Seats");
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    body = getBody(response);
    assertXpathEvaluatesTo("5", "/atom:entry/atom:content/m:properties/d:Seats", body);
    assertXpathNotExists("/atom:entry/atom:content/m:properties/d:Id", body);
  }
}
