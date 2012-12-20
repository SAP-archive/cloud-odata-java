package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.ContentType;

/**
 * Tests employing the reference scenario reading links in XML format
 * @author SAP AG
 */
public final class LinksXmlReadOnlyTest extends AbstractRefTest {

  @Test
  public void singleLink() throws Exception {
    HttpResponse response = callUri("Employees('6')/$links/ne_Room");
    checkMediaType(response, ContentType.APPLICATION_XML);
    assertTrue(getBody(response).contains("Rooms('2')</uri>"));

    response = callUri("Managers('3')/$links/nm_Employees('5')");
    checkMediaType(response, ContentType.APPLICATION_XML);
    assertTrue(getBody(response).contains("5"));

    response = callUri("Teams('3')/nt_Employees('6')/$links/ne_Room");
    checkMediaType(response, ContentType.APPLICATION_XML);
    assertTrue(getBody(response).contains("Rooms('2')</uri>"));

    response = callUri("Employees('6')/ne_Manager/$links/nm_Employees('3')");
    checkMediaType(response, ContentType.APPLICATION_XML);
    assertTrue(getBody(response).contains("Employees('3')</uri>"));

    badRequest("Employees('6')/$links/");
    badRequest("Employees('6')/ne_Manager/$links");
  }
}
