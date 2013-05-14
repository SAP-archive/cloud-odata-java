package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.ODataHttpHeaders;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.ep.JsonEntityProvider;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonPropertyProducerTest extends BaseTest {

  @Test
  public void serializeString() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeName");

    final ODataResponse response = new JsonEntityProvider().writeProperty(property, "\"Игорь\tНиколаевич\tЛарионов\"");
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"EmployeeName\":\"\\\"Игорь\\tНиколаевич\\tЛарионов\\\"\"}}", json);
  }

  @Test
  public void serializeVeryLongString() throws Exception {
    char[] chars = new char[32768];
    Arrays.fill(chars, 0, 32768, 'a');
    String propertyValue = new String(chars);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeName");

    final ODataResponse response = new JsonEntityProvider().writeProperty(property, propertyValue);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"EmployeeName\":\"" + propertyValue + "\"}}", json);
  }

  @Test
  public void serializeNumber() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, 42);
    assertEquals("{\"d\":{\"Age\":42}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeBinary() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Building").getProperty("Image");
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, new byte[] { 42, -42 });
    assertEquals("{\"d\":{\"Image\":\"KtY=\"}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeBinaryWithContentType() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario2", "Photo").getProperty("Image");
    Map<String, Object> content = new HashMap<String, Object>();
    content.put("getImageType", "image/jpeg");
    content.put("Image", new byte[] { 1, 2, 3 });
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, content);
    assertEquals("{\"d\":{\"Image\":\"AQID\"}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeBoolean() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Team").getProperty("isScrumTeam");
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, false);
    assertEquals("{\"d\":{\"isScrumTeam\":false}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeNull() throws Exception {
    EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("ImageUrl");
    ODataResponse response = new JsonEntityProvider().writeProperty(property, null);
    assertEquals("{\"d\":{\"ImageUrl\":null}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));

    property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    response = new JsonEntityProvider().writeProperty(property, null);
    assertEquals("{\"d\":{\"Age\":null}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));

    property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");
    response = new JsonEntityProvider().writeProperty(property, null);
    assertEquals("{\"d\":{\"EntryDate\":null}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));

    property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Building").getProperty("Image");
    response = new JsonEntityProvider().writeProperty(property, null);
    assertEquals("{\"d\":{\"Image\":null}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeDateTime() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");
    Calendar dateTime = Calendar.getInstance();
    dateTime.setTimeInMillis(-42);
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, dateTime);
    assertEquals("{\"d\":{\"EntryDate\":\"\\/Date(-42)\\/\"}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeComplexProperty() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");
    Map<String, Object> cityData = new LinkedHashMap<String, Object>();
    cityData.put("PostalCode", "8392");
    cityData.put("CityName", "Å");
    Map<String, Object> locationData = new LinkedHashMap<String, Object>();
    locationData.put("City", cityData);
    locationData.put("Country", "NO");

    final ODataResponse response = new JsonEntityProvider().writeProperty(property, locationData);
    assertEquals("{\"d\":{\"Location\":{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":\"8392\",\"CityName\":\"Å\"},\"Country\":\"NO\"}}}",
        StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }

  @Test
  public void serializeComplexPropertyNull() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Location");
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, null);
    assertEquals("{\"d\":{\"Location\":{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":null,\"CityName\":null},\"Country\":null}}}",
        StringHelper.inputStreamToString((InputStream) response.getEntity()));
  }
}
