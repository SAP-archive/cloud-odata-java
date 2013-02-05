package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario changing links in XML format
 * @author SAP AG
 */
public final class LinksXmlChangeTest extends AbstractRefTest {

  @Test
  public void createLink() throws Exception {
    final String uriString = "Rooms('101')/$links/nr_Employees";
    final String requestBody = getBody(callUri(uriString.replace("'101'", "'1'") + "('1')"));
    postUri(uriString, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertEquals(requestBody, getBody(callUri(uriString + "('1')")));

    postUri(uriString, requestBody.replace("'1'", "'99'"), HttpContentType.APPLICATION_XML, HttpStatusCodes.NOT_FOUND);
  }
}
