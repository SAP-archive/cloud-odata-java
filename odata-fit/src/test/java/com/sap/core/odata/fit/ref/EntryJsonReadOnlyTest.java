package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

/**
 * Tests employing the reference scenario reading a single entity in JSON format.
 * @author SAP AG
 */
public class EntryJsonReadOnlyTest extends AbstractRefTest {

  @Test
  public void entry() throws Exception {
    final HttpResponse response = callUri("Rooms('3')?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    checkEtag(response, "W/\"3\"");
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"3\",\"Name\":\"Room 3\",\"Seats\":2,\"Version\":3,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Building\"}}}}",
        getBody(response));
  }

  @Test
  public void mediaLinkEntry() throws Exception {
    final HttpResponse response = callUri("Employees('3')?$select=Age,EntryDate,ne_Team&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Employees('3')\","
        + "\"uri\":\"" + getEndpoint() + "Employees('3')\",\"type\":\"RefScenario.Employee\","
        + "\"content_type\":\"image/jpeg\",\"media_src\":\"Employees('3')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Employees('3')/$value\"},"
        + "\"Age\":56,\"EntryDate\":null,"
        + "\"ne_Team\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Employees('3')/ne_Team\"}}}}",
        getBody(response));
  }
}
