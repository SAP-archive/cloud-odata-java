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
 * Tests employing the reference scenario changing entities in XML format
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
