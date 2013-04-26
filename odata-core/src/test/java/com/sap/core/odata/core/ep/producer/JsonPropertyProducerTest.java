package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
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
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeId");

    final ODataResponse response = new JsonEntityProvider().writeProperty(property, "1");
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());
    assertEquals(ODataServiceVersion.V10, response.getHeader(ODataHttpHeaders.DATASERVICEVERSION));

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"EmployeeId\":\"1\"}}", json);
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
  public void serializeNull() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("ImageUrl");
    final ODataResponse response = new JsonEntityProvider().writeProperty(property, null);
    assertEquals("{\"d\":{\"ImageUrl\":null}}", StringHelper.inputStreamToString((InputStream) response.getEntity()));
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
}
