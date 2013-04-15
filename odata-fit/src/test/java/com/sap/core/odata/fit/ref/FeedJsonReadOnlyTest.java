/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

/**
 * Tests employing the reference scenario reading entity sets in JSON format.
 * @author SAP AG
 */
public class FeedJsonReadOnlyTest extends AbstractRefTest {

  @Test
  public void feed() throws Exception {
    final HttpResponse response = callUri("Buildings()?$top=2&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"1\",\"Name\":\"Building 1\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('1')/nb_Rooms\"}}},"
        + "{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('2')/nb_Rooms\"}}}"
        + "]}}",
        getBody(response));
  }

  @Test
  public void feedwithCountAndNext() throws Exception {
    final HttpResponse response = callUri("Rooms()?$inlinecount=allpages&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    final String json = getBody(response);
    assertTrue(json.startsWith("{\"d\":{\"__count\":\"103\",\"results\":["));
    assertTrue(json.endsWith("],\"__next\":\"Rooms?$skiptoken=97&$inlinecount=allpages\"}}"));
  }

  @Test
  public void feedWithInlineEntry() throws Exception {
    final HttpResponse response = callUri("Rooms()?$expand=nr_Building&$top=2&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"1\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 1\",\"Seats\":1,\"Version\":1,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('1')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"1\",\"Name\":\"Building 1\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('1')/nb_Rooms\"}}}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('10')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('10')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"1\\\"\"},"
        + "\"Id\":\"10\",\"Name\":\"Room 10\",\"Seats\":6,\"Version\":1,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('10')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('3')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('3')\",\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"3\",\"Name\":\"Building 3\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('3')/nb_Rooms\"}}}}]}}",
        getBody(response));
  }

  @Test
  public void feedWithInlineFeed() throws Exception {
    final HttpResponse response = callUri("Buildings()?$expand=nb_Rooms&$top=2&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"1\",\"Name\":\"Building 1\",\"Image\":null,"
        + "\"nb_Rooms\":{\"results\":[{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"1\\\"\"},"
        + "\"Id\":\"1\",\"Name\":\"Room 1\",\"Seats\":1,\"Version\":1,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('1')/nr_Building\"}}}]}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"type\":\"RefScenario.Building\"},"
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
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Rooms('3')/nr_Building\"}}}]}}]}}",
        getBody(response));
  }

  @Test
  public void feedWithTwoLevelInline() throws Exception {
    final HttpResponse response = callUri("Employees()?$expand=ne_Room/nr_Building&$select=Age,ne_Room/Seats,ne_Room/nr_Building/Name&$top=2&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Employees('1')\","
        + "\"uri\":\"" + getEndpoint() + "Employees('1')\",\"type\":\"RefScenario.Employee\","
        + "\"content_type\":\"image/jpeg\",\"media_src\":\"Employees('1')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Employees('1')/$value\"},"
        + "\"Age\":52,"
        + "\"ne_Room\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('1')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('1')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"1\\\"\"},\"Seats\":1,"
        + "\"nr_Building\":{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"type\":\"RefScenario.Building\"},\"Name\":\"Building 1\"}}},"
        + "{\"__metadata\":{\"id\":\"" + getEndpoint() + "Employees('2')\","
        + "\"uri\":\"" + getEndpoint() + "Employees('2')\",\"type\":\"RefScenario.Employee\","
        + "\"content_type\":\"image/jpeg\",\"media_src\":\"Employees('2')/$value\","
        + "\"edit_media\":\"" + getEndpoint() + "Employees('2')/$value\"},"
        + "\"Age\":32,"
        + "\"ne_Room\":{\"__metadata\":{\"id\":\"" + getEndpoint() + "Rooms('2')\","
        + "\"uri\":\"" + getEndpoint() + "Rooms('2')\",\"type\":\"RefScenario.Room\","
        + "\"etag\":\"W/\\\"2\\\"\"},\"Seats\":5,"
        + "\"nr_Building\":{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"type\":\"RefScenario.Building\"},\"Name\":\"Building 2\"}}}]}}",
        getBody(response));
  }
}