package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario changing links in JSON format.
 * @author SAP AG
 */
public final class LinksJsonChangeTest extends AbstractRefTest {

  @Test
  public void createLink() throws Exception {
    final String uriString = "Rooms('3')/$links/nr_Employees";
    final String requestBody = "{\"uri\":\"" + getEndpoint() + "Employees('6')\"}";
    postUri(uriString, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("{\"d\":" + requestBody + "}", getBody(callUri(uriString + "('6')?$format=json")));
  }

  @Test
  public void updateLink() throws Exception {
    final String uriString = "Employees('2')/$links/ne_Room";
    final String requestBody = "{\"uri\":\"" + getEndpoint() + "Rooms('3')\"}";
    putUri(uriString, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("{\"d\":" + requestBody + "}", getBody(callUri(uriString + "?$format=json")));
  }
}
