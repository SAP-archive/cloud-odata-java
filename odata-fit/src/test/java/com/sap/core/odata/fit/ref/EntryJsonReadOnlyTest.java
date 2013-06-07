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

  @Test
  public void entryFromNonDefaultContainer() throws Exception {
    final HttpResponse response = callUri("Container2.Photos(Id=1,Type='image%2Fpng')?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Container2.Photos(Id=1,Type='image%2Fpng')\","
        + "\"uri\":\"" + getEndpoint() + "Container2.Photos(Id=1,Type='image%2Fpng')\","
        + "\"type\":\"RefScenario2.Photo\",\"etag\":\"W/\\\"1\\\"\",\"content_type\":\"image/png\","
        + "\"media_src\":\"Container2.Photos(Id=1,Type='image%2Fpng')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Container2.Photos(Id=1,Type='image%2Fpng')/$value\"},"
        + "\"Id\":1,\"Name\":\"Photo 1\",\"Type\":\"image/png\","
        + "\"ImageUrl\":\"http://localhost/male_1_WinterW.jpg\","
        + "\"Image\":\"" + PHOTO_DEFAULT_IMAGE + "\",\"BinaryData\":null,"
        + "\"Содержание\":\"Образ\"}}",
        getBody(response));
  }

  @Test
  public void entryWithInlineEntry() throws Exception {
    final HttpResponse response = callUri("Rooms('3')?$expand=nr_Building&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"3\",\"Name\":\"Room 3\",\"Seats\":2,\"Version\":3,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('2')/nb_Rooms\"}}}}}",
        getBody(response));
  }

  @Test
  public void entryWithInlineFeed() throws Exception {
    final HttpResponse response = callUri("Buildings('2')?$expand=nb_Rooms&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":{\"results\":[{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('2')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('2')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"2\\\"\"},"
        + "\"Id\":\"2\",\"Name\":\"Room 2\",\"Seats\":5,\"Version\":2,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('2')/nr_Building\"}}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Id\":\"3\",\"Name\":\"Room 3\",\"Seats\":2,\"Version\":3,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Building\"}}}]}}}",
        getBody(response));
  }

  @Test
  public void entryWithTwoLevelInline() throws Exception {
    HttpResponse response = callUri("Employees('5')?$expand=ne_Room/nr_Building&$select=Age,ne_Room/Seats,ne_Room/nr_Building/Name&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Employees('5')\","
        + "\"uri\":\"" + getEndpoint() + "Employees('5')\","
        + "\"type\":\"RefScenario.Employee\",\"content_type\":\"image/jpeg\","
        + "\"media_src\":\"Employees('5')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Employees('5')/$value\"},"
        + "\"Age\":42,"
        + "\"ne_Room\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('3')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('3')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"3\\\"\"},"
        + "\"Seats\":2,"
        + "\"nr_Building\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"type\":\"RefScenario.Building\"},\"Name\":\"Building 2\"}}}}",
        getBody(response));

    response = callUri("Employees('1')?$expand=ne_Room/nr_Building&$select=EntryDate,ne_Manager,ne_Room/*,ne_Room/nr_Building/Name&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Employees('1')\","
        + "\"uri\":\"" + getEndpoint() + "Employees('1')\","
        + "\"type\":\"RefScenario.Employee\",\"content_type\":\"image/jpeg\","
        + "\"media_src\":\"Employees('1')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Employees('1')/$value\"},"
        + "\"EntryDate\":\"\\/Date(915148800000)\\/\","
        + "\"ne_Manager\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Employees('1')/ne_Manager\"}},"
        + "\"ne_Room\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"1\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 1\",\"Seats\":1,\"Version\":1,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('1')\",\"type\":\"RefScenario.Building\"},"
        + "\"Name\":\"Building 1\"}}}}",
        getBody(response));
  }
}
