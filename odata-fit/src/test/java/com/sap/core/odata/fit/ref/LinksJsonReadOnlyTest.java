package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

/**
 * Tests employing the reference scenario reading links in JSON format.
 * @author SAP AG
 */
public final class LinksJsonReadOnlyTest extends AbstractRefTest {

  @Test
  public void singleLink() throws Exception {
    HttpResponse response = callUri("Employees('1')/$links/ne_Room?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"uri\":\"" + getEndpoint() + "Rooms('1')\"}}", getBody(response));
  }

  @Test
  public void links() throws Exception {
//    HttpResponse response = callUri("Rooms('1')/$links/nr_Employees?$format=json");
//    checkMediaType(response, HttpContentType.APPLICATION_JSON);
//    assertEquals("{\"d\":[{\"uri\":\"" + getEndpoint() + "Employees('1')\"}]}", getBody(response));
//
//    response = callUri("Rooms('2')/$links/nr_Employees?$skip=99&$inlinecount=allpages&$format=json");
//    checkMediaType(response, HttpContentType.APPLICATION_JSON);
//    assertEquals("{\"d\":{\"__count\":\"4\",\"results\":[]}}", getBody(response));
  }
}
