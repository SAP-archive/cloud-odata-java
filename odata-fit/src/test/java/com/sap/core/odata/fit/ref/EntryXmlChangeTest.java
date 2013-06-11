package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.http.HttpResponse;
import org.custommonkey.xmlunit.XMLAssert;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.edm.Edm;

/**
 * Tests employing the reference scenario changing entities in XML format.
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
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertEquals(getEndpoint() + "Teams('4')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
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
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertEquals(getEndpoint() + "Rooms('104')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    checkEtag(response, "W/\"2\"");
    assertXpathEvaluatesTo("4", "/atom:entry/atom:content/m:properties/d:Seats", getBody(response));
    checkUri("Rooms('104')/nr_Employees('4')");
    checkUri("Rooms('104')/nr_Employees('5')");
  }

  @Test
  public void createInvalidXml() throws Exception {
    getBody(callUri("Employees('7')", HttpStatusCodes.NOT_FOUND));

    final String updateBody = "<invalidXml></invalid>";
    final HttpResponse postResult = postUri("Employees", updateBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.CREATED);
    checkMediaType(postResult, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertXpathEvaluatesTo("7", "/atom:entry/m:properties/d:EmployeeId", getBody(postResult));

    final String requestBodyAfter = getBody(callUri("Employees('7')"));
    assertXpathEvaluatesTo("7", "/atom:entry/m:properties/d:EmployeeId", requestBodyAfter);
  }

  @Test
  public void createMediaResource() throws Exception {
    HttpResponse response = postUri("Employees()", "plain text", HttpContentType.TEXT_PLAIN, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertEquals(getEndpoint() + "Employees('7')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertXpathEvaluatesTo("7", "/atom:entry/m:properties/d:EmployeeId", getBody(response));
    response = callUri("Employees('7')/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN);
    assertEquals("plain text", getBody(response));

    response = postUri("Container2.Photos", "dummy", HttpContentType.TEXT_PLAIN, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertEquals(getEndpoint() + "Container2.Photos(Id=5,Type='application%2Foctet-stream')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    checkEtag(response, "W/\"5\"");
    assertXpathEvaluatesTo("Photo 5", "/atom:entry/m:properties/d:Name", getBody(response));
    response = callUri("Container2.Photos(Id=5,Type='application%2Foctet-stream')/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN);
    assertEquals("dummy", getBody(response));
  }

  @Test
  public void createMediaResourceWithNavigation() throws Exception {
    HttpResponse response = postUri("Teams('1')/nt_Employees", "X", HttpContentType.TEXT_PLAIN, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertEquals(getEndpoint() + "Employees('7')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    assertXpathEvaluatesTo("7", "/atom:entry/m:properties/d:EmployeeId", getBody(response));

    response = callUri("Employees('7')/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN);
    assertEquals("X", getBody(response));
  }

  @Test
  public void createEntryWithInlineFeed() throws Exception {
    final String buildingWithRooms = "<atom:entry xml:base=\"" + getEndpoint() + "\""
        + " xmlns:atom=\"" + Edm.NAMESPACE_ATOM_2005 + "\""
        + " xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\""
        + " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">"
        + "<atom:content type=\"application/xml\">"
        + "  <m:properties><d:Id>1</d:Id><d:Name>Building 1</d:Name></m:properties>"
        + "</atom:content>"
        + "<atom:id>Buildings('1')</atom:id>"
        + "<atom:link href=\"Buildings('1')/nb_Rooms\" rel=\"" + Edm.NAMESPACE_REL_2007_08 + "nb_Rooms\""
        + " type=\"application/atom+xml;type=feed\" title=\"included Rooms\">"
        + "  <m:inline>"
        + "    <atom:feed>"
        + "      <atom:author><atom:name/></atom:author>"
        + "      <atom:id>Rooms</atom:id>"
        + "      <atom:entry>"
        + "        <atom:content type=\"application/xml\">"
        + "          <m:properties>"
        + "            <d:Id>1</d:Id><d:Name>Room 1</d:Name><d:Seats>1</d:Seats><d:Version>1</d:Version>"
        + "          </m:properties>"
        + "        </atom:content>"
        + "        <atom:id>Rooms('1')</atom:id>"
        + "      </atom:entry>"
        + "      <atom:entry>"
        + "        <atom:content type=\"application/xml\">"
        + "          <m:properties>"
        + "            <d:Id>2</d:Id><d:Name>Room 2</d:Name><d:Seats>5</d:Seats><d:Version>2</d:Version>"
        + "          </m:properties>"
        + "        </atom:content>"
        + "        <atom:id>Rooms('2')</atom:id>"
        + "      </atom:entry>"
        + "    </atom:feed>"
        + "  </m:inline>"
        + "</atom:link>"
        + "<atom:title type=\"text\">Buildings('1')</atom:title>"
        + "<atom:updated>2012-02-29T11:59:59Z</atom:updated>"
        + "</atom:entry>";

    HttpResponse response = postUri("Buildings", buildingWithRooms, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.CREATED);
    checkMediaType(response, HttpContentType.APPLICATION_ATOM_XML_UTF8 + ";type=entry");
    assertEquals(getEndpoint() + "Buildings('4')", response.getFirstHeader(HttpHeaders.LOCATION).getValue());
    final String body = getBody(response);
    assertXpathEvaluatesTo("4", "/atom:entry/atom:content/m:properties/d:Id", body);
    assertXpathEvaluatesTo("105", "/atom:entry/atom:link[@rel='" + Edm.NAMESPACE_REL_2007_08 + "nb_Rooms']/m:inline/atom:feed/atom:entry[2]/atom:content/m:properties/d:Id", body);
    checkUri("Buildings('4')");
    checkUri("Rooms('104')");
    checkUri("Buildings('4')/nb_Rooms('104')");
    checkUri("Rooms('104')/nr_Building");
    checkUri("Rooms('105')");
    checkUri("Buildings('4')/nb_Rooms('105')");
    checkUri("Rooms('105')/nr_Building");
    assertEquals("5", getBody(callUri("Buildings('4')/nb_Rooms('105')/Seats/$value")));
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
  public void updateUnknownProperty() throws Exception {
    final String requestBody = getBody(callUri("Employees('1')"))
        .replace("<d:Age>52</d:Age>", "<d:Age>33</d:Age><d:SomeUnknownTag>SomeUnknownValue</d:SomeUnknownTag>");

    putUri("Employees('1')", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.BAD_REQUEST);
    // check nothing has changed
    assertXpathEvaluatesTo("52", "/atom:entry/m:properties/d:Age", getBody(callUri("Employees('1')")));
  }

  @Test
  public void updateInvalidXml() throws Exception {
    final String requestBodyBefore = getBody(callUri("Employees('2')"));

    putUri("Employees('2')", "<invalidXml></invalid>", HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.BAD_REQUEST);

    assertEquals(requestBodyBefore, getBody(callUri("Employees('2')")));
  }

  @Test
  public void patchAndMerge() throws Exception {
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
    HttpResponse response = callUri(ODataHttpMethod.MERGE, "Rooms('3')", HttpHeaders.IF_MATCH, "W/\"3\"", requestBody, HttpContentType.APPLICATION_ATOM_XML_ENTRY, HttpStatusCodes.NO_CONTENT);
    checkEtag(response, "W/\"3\"");
    assertXpathEvaluatesTo("Room X", "/atom:entry/atom:content/m:properties/d:Name", getBody(callUri("Rooms('3')")));
  }

  @Test
  public void delete() throws Exception {
    final String uri = "Employees('2')";
    deleteUriOk(uri);
    final String requestBody = getBody(callUri(uri, HttpStatusCodes.NOT_FOUND));

    XMLAssert.assertXpathExists("/m:error", requestBody);
    assertXpathEvaluatesTo("Requested entity could not be found.", "/m:error/m:message", requestBody);
  }
}
