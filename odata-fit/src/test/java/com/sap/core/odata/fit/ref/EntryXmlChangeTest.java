package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.core.commons.ODataHttpMethod;

/**
 * Tests employing the reference scenario changing entities in XML format
 * @author SAP AG
 */
public class EntryXmlChangeTest extends AbstractRefXmlTest {

  @Test
  public void create() throws Exception {
    // Create an entry for a type that has no media resource.
    String requestBody = getBody(callUri("Teams('1')"))
        .replace("'1'", "'9'")
        .replace("Id>1", "Id>9")
        .replace("Team 1", "Team X")
        .replaceAll("<link.+?/>", "");
    HttpResponse response = postUri("Teams()", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertEquals(getEndpoint() + "Teams('4')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    assertXpathEvaluatesTo("Team X", "/atom:entry/atom:content/m:properties/d:Name", getBody(response));

    // Create an entry for a type that has no media resource.
    // Add navigation to Employee('4') and Employee('5').
    requestBody = "<entry xmlns=\"" + Edm.NAMESPACE_ATOM_2005 + "\"" + "\n"
        + "       xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\"" + "\n"
        + "       xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">" + "\n"
        + "  <author><name>no author</name></author>" + "\n"
        + "  <content type=\"application/xml\">" + "\n"
        + "    <m:properties>" + "\n"
        + "      <d:Id>109</d:Id>" + "\n"
        + "      <d:Name/>" + "\n"
        + "      <d:Seats>4</d:Seats>" + "\n"
        + "      <d:Version>2</d:Version>" + "\n"
        + "    </m:properties>" + "\n"
        + "  </content>" + "\n"
        + "  <id>Rooms('104')</id>" + "\n"
        + "  <title>Room 104</title>" + "\n"
        + "  <updated>2011-08-10T12:00:23Z</updated>" + "\n"
        + "  <link href=\"Employees('4')\"" + "\n"
        + "        rel=\"" + Edm.NAMESPACE_REL_2007_08 + "nr_Employees\"" + "\n"
        + "        type=\"" + HttpContentType.APPLICATION_ATOM_XML_FEED_UTF8 + "\"/>" + "\n"
        + "  <link href=\"Employees('5')\"" + "\n"
        + "        rel=\"" + Edm.NAMESPACE_REL_2007_08 + "nr_Employees\"" + "\n"
        + "        type=\"" + HttpContentType.APPLICATION_ATOM_XML_FEED_UTF8 + "\"/>" + "\n"
        + "</entry>";
    response = postUri("Rooms", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertEquals(getEndpoint() + "Rooms('104')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    assertXpathEvaluatesTo("4", "/atom:entry/atom:content/m:properties/d:Seats", getBody(response));
    // checkUri("Rooms('104')/nr_Employees('4')");
    checkUri("Rooms('104')/nr_Employees('5')");
  }

  @Test
  public void createMediaResource() throws Exception {
    HttpResponse response = postUri("Employees()", "plain text", HttpContentType.TEXT_PLAIN, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + "; type=entry");
    assertEquals(getEndpoint() + "Employees('7')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    assertXpathEvaluatesTo("7", "/atom:entry/m:properties/d:EmployeeId", getBody(response));
    response = callUri("Employees('7')/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN);
    assertEquals("plain text", getBody(response));

    postUri("Teams('1')/nt_Employees", "X", HttpContentType.TEXT_PLAIN, HttpStatusCodes.NOT_IMPLEMENTED);
  }

  @Test
  public void update() throws Exception {
    final String requestBody = getBody(callUri("Employees('1')"))
        .replace("'1'", "'9'")
        .replace("EmployeeId>1", "EmployeeId>9")
        .replace(EMPLOYEE_1_NAME, "Mister X")
        .replaceAll("<link.+?/>", "");
    putUri("Employees('2')", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("Mister X", "/atom:entry/m:properties/d:EmployeeName", getBody(callUri("Employees('2')")));
  }

  @Test
  public void patch() throws Exception {
    String requestBody = "<entry xmlns=\"" + Edm.NAMESPACE_ATOM_2005 + "\"" + "\n"
        + "       xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\"" + "\n"
        + "       xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">" + "\n"
        + "  <content/>" + "\n"
        + "  <m:properties>" + "\n"
        + "    <d:Location>" + "\n"
        + "      <d:City>" + "\n"
        + "        <d:PostalCode>69124</d:PostalCode>" + "\n"
        + "        <d:CityName>" + CITY_1_NAME + "</d:CityName>" + "\n"
        + "      </d:City>" + "\n"
        + "      <d:Country>Germany</d:Country>" + "\n"
        + "    </d:Location>" + "\n"
        + "    <d:EntryDate m:null=\"true\"/>" + "\n"
        + "  </m:properties>" + "\n"
        + "</entry>";
    callUri(ODataHttpMethod.PATCH, "Employees('2')", null, null, requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo(CITY_1_NAME, "/atom:entry/m:properties/d:Location/d:City/d:CityName", getBody(callUri("Employees('2')")));

    requestBody = "<entry xmlns=\"" + Edm.NAMESPACE_ATOM_2005 + "\">" + "\n"
        + "  <content xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\"" + "\n"
        + "           xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\"" + "\n"
        + "           type=\"" + HttpContentType.APPLICATION_XML_UTF8 + "\">" + "\n"
        + "    <m:properties><d:Name>Room X</d:Name></m:properties>" + "\n"
        + "  </content>" + "\n"
        + "</entry>";
    callUri(ODataHttpMethod.MERGE, "Rooms('3')", HttpHeaders.IF_MATCH, "W/\"3\"", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("Room X", "/atom:entry/atom:content/m:properties/d:Name", getBody(callUri("Rooms('3')")));
  }
}
