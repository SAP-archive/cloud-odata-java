package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.JsonEntityProvider;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonFunctionImportTest extends BaseTest {

  @Test
  public void singleSimpleType() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("MaximalAge");

    final ODataResponse response = new JsonEntityProvider().writeFunctionImport(functionImport, 42, null);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"MaximalAge\":42}}", json);
  }

  @Test
  public void singleComplexType() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("MostCommonLocation");
    Map<String, Object> cityData = new HashMap<String, Object>();
    cityData.put("PostalCode", "8392");
    cityData.put("CityName", "Å");
    Map<String, Object> locationData = new HashMap<String, Object>();
    locationData.put("City", cityData);
    locationData.put("Country", "NO");

    final ODataResponse response = new JsonEntityProvider().writeFunctionImport(functionImport, locationData, null);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"MostCommonLocation\":{"
        + "\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},\"PostalCode\":\"8392\","
        + "\"CityName\":\"Å\"},\"Country\":\"NO\"}}}",
        json);
  }

  @Test
  public void collectionOfSimpleTypes() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("AllUsedRoomIds");

    final ODataResponse response = new JsonEntityProvider().writeFunctionImport(functionImport, Arrays.asList("1","2","3"), null);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{\"type\":\"Collection(Edm.String)\"},"
        + "\"results\":[\"1\",\"2\",\"3\"]}}",
        json);
  }

  @Test
  public void collectionOfComplexTypes() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("AllLocations");
    Map<String, Object> locationData = new HashMap<String, Object>();
    locationData.put("Country", "NO");
    List<Map<String, Object>> locations = new ArrayList<Map<String, Object>>();
    locations.add(locationData);

    final ODataResponse response = new JsonEntityProvider().writeFunctionImport(functionImport, locations, null);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{\"type\":\"Collection(RefScenario.c_Location)\"},"
        + "\"results\":[{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":null,\"CityName\":null},\"Country\":\"NO\"}]}}",
        json);
  }

  @Test
  public void singleEntityType() throws Exception {
    final EdmFunctionImport functionImport = MockFacade.getMockEdm().getDefaultEntityContainer().getFunctionImport("OldestEmployee");
    final String uri = "http://host:80/service/";
    final EntityProviderProperties properties =
        EntityProviderProperties.serviceRoot(URI.create(uri)).build();
    Map<String, Object> employeeData = new HashMap<String, Object>();
    employeeData.put("EmployeeId", "1");
    employeeData.put("getImageType", "image/jpeg");

    final ODataResponse response = new JsonEntityProvider().writeFunctionImport(functionImport, employeeData, properties);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{"
        + "\"id\":\"" + uri + "Employees('1')\","
        + "\"uri\":\"" + uri + "Employees('1')\","
        + "\"type\":\"RefScenario.Employee\",\"content_type\":\"image/jpeg\","
        + "\"media_src\":\"Employees('1')/$value\","
        + "\"edit_media\":\"" + uri + "Employees('1')/$value\"},"
        + "\"EmployeeId\":\"1\",\"EmployeeName\":null,"
        + "\"ManagerId\":null,\"RoomId\":null,\"TeamId\":null,"
        + "\"Location\":{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":null,\"CityName\":null},\"Country\":null},\"Age\":null,"
        + "\"EntryDate\":null,\"ImageUrl\":null,"
        + "\"ne_Manager\":{\"__deferred\":{\"uri\":\"" + uri + "Employees('1')/ne_Manager\"}},"
        + "\"ne_Team\":{\"__deferred\":{\"uri\":\"" + uri + "Employees('1')/ne_Team\"}},"
        + "\"ne_Room\":{\"__deferred\":{\"uri\":\"" + uri + "Employees('1')/ne_Room\"}}}}",
        json);
  }
}
