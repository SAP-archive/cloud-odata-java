package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.ContentType;

/**
 * Tests employing the reference scenario reading a single entity in XML format
 * @author SAP AG
 */
public class EntryXmlReadOnlyTest extends AbstractRefTest {

  @Test
  public void entry() throws Exception {
    HttpResponse response = callUri("Employees('2')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertTrue(getBody(response).contains(EMPLOYEE_2_NAME));

    response = callUri("Managers('3')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(EMPLOYEE_3_NAME));

    response = callUri("Teams('2')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(">2</"));

    response = callUri("Rooms('1')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    checkEtag(response, "W/\"1\"");
    assertTrue(getBody(response).contains(">1</"));

    response = callUri("Rooms('1')?$expand=nr_Employees");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    // assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertFalse(getBody(response).isEmpty());
    // assertTrue(getBody(response).contains(EMPLOYEE_1_NAME));

    response = callUri("Container2.Photos(Id=1,Type='image%2Fpng')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(">image/png</"));

    response = callUri("Container2.Photos(Id=4,Type='foo')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains("src=\"Container2.Photos(Id=4,Type='foo')/$value\""));

    notFound("Managers('5')");
    badRequest("Managers('3')?$top=1");
    badRequest("Rooms(X'33')");
  }

  @Test
  public void navigationEntry() throws Exception {
    HttpResponse response = callUri("Employees('2')/ne_Manager");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(MANAGER_NAME));

    response = callUri("Employees('2')/ne_Team/nt_Employees('1')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(EMPLOYEE_1_NAME));

    response = callUri("Employees('2')/ne_Manager/nm_Employees('6')");
    checkMediaType(response, ContentType.APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(EMPLOYEE_6_NAME));

    // notFound("Employees('2')/ne_Manager()");
  }
}
