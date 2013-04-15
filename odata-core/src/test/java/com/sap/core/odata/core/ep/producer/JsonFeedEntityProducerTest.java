/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.JsonEntityProvider;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonFeedEntityProducerTest extends BaseTest {
  protected static final String BASE_URI = "http://host:80/service/";
  protected static final EntityProviderWriteProperties DEFAULT_PROPERTIES =
      EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI)).build();

  @Test
  public void feed() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    Map<String, Object> team1Data = new HashMap<String, Object>();
    team1Data.put("Id", "1");
    team1Data.put("isScrumTeam", true);
    Map<String, Object> team2Data = new HashMap<String, Object>();
    team2Data.put("Id", "2");
    team2Data.put("isScrumTeam", false);
    List<Map<String, Object>> teamsData = new ArrayList<Map<String, Object>>();
    teamsData.add(team1Data);
    teamsData.add(team2Data);

    final ODataResponse response = new JsonEntityProvider().writeFeed(entitySet, teamsData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{\"id\":\"" + BASE_URI + "Teams('1')\","
        + "\"uri\":\"" + BASE_URI + "Teams('1')\",\"type\":\"RefScenario.Team\"},"
        + "\"Id\":\"1\",\"Name\":null,\"isScrumTeam\":true,"
        + "\"nt_Employees\":{\"__deferred\":{\"uri\":\"" + BASE_URI + "Teams('1')/nt_Employees\"}}},"
        + "{\"__metadata\":{\"id\":\"" + BASE_URI + "Teams('2')\","
        + "\"uri\":\"" + BASE_URI + "Teams('2')\",\"type\":\"RefScenario.Team\"},"
        + "\"Id\":\"2\",\"Name\":null,\"isScrumTeam\":false,"
        + "\"nt_Employees\":{\"__deferred\":{\"uri\":\"" + BASE_URI + "Teams('2')/nt_Employees\"}}}]}}",
        json);
  }

  @Test
  public void inlineCount() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Buildings");
    final ODataResponse response = new JsonEntityProvider().writeFeed(entitySet, new ArrayList<Map<String, Object>>(),
        EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI))
            .inlineCountType(InlineCount.ALLPAGES).inlineCount(42)
            .build());
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__count\":\"42\",\"results\":[]}}", json);
  }

  @Test
  public void nextLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    Map<String, Object> roomData = new HashMap<String, Object>();
    roomData.put("Id", "1");
    roomData.put("Seats", 123);
    roomData.put("Version", 1);
    List<Map<String, Object>> roomsData = new ArrayList<Map<String, Object>>();
    roomsData.add(roomData);

    final ODataResponse response = new JsonEntityProvider().writeFeed(entitySet, roomsData,
        EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI)).nextLink("Rooms?$skiptoken=2").build());
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{\"id\":\"" + BASE_URI + "Rooms('1')\","
        + "\"uri\":\"" + BASE_URI + "Rooms('1')\",\"type\":\"RefScenario.Room\",\"etag\":\"W/\\\"1\\\"\"},"
        + "\"Id\":\"1\",\"Name\":null,\"Seats\":123,\"Version\":1,"
        + "\"nr_Employees\":{\"__deferred\":{\"uri\":\"" + BASE_URI + "Rooms('1')/nr_Employees\"}},"
        + "\"nr_Building\":{\"__deferred\":{\"uri\":\"" + BASE_URI + "Rooms('1')/nr_Building\"}}}],"
        + "\"__next\":\"Rooms?$skiptoken=2\"}}",
        json);
  }
}
