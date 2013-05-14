package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.edm.Edm;

/**
 * Tests employing the reference scenario changing links in XML format
 * @author SAP AG
 */
public final class LinksXmlChangeTest extends AbstractRefTest {

  private static final String XML_DECLARATION = "<?xml version='1.0' encoding='utf-8'?>";

  @Test
  public void createLink() throws Exception {
    final String uriString = "Rooms('101')/$links/nr_Employees";
    final String requestBody = XML_DECLARATION
        + "<uri xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">" + getEndpoint() + "Employees('1')</uri>";
    postUri(uriString, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertEquals(requestBody, getBody(callUri(uriString + "('1')")));

    postUri(uriString, requestBody.replace("'1'", "'99'"), HttpContentType.APPLICATION_XML, HttpStatusCodes.NOT_FOUND);
  }

  @Test
  public void updateLink() throws Exception {
    final String uriString = "Employees('2')/$links/ne_Room";
    final String requestBody =
        "<uri xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">" + getEndpoint() + "Rooms('3')</uri>";
    putUri(uriString, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertEquals(XML_DECLARATION + requestBody, getBody(callUri(uriString)));

    final String uriString2 = "Rooms('1')/$links/nr_Employees('1')";
    callUri(ODataHttpMethod.PATCH, uriString2, null, null, requestBody.replace("Rooms", "Employees"), HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    notFound(uriString2);
    checkUri(uriString2.replace("Employees('1')", "Employees('3')"));

    putUri(uriString.replace("'2'", "'99'"), requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NOT_FOUND);
    putUri(uriString, requestBody.replace("'3'", "'999'"), HttpContentType.APPLICATION_XML, HttpStatusCodes.NOT_FOUND);
    putUri("Teams('1')/nt_Employees('2')/$links/ne_Room", requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.BAD_REQUEST);
  }
}
