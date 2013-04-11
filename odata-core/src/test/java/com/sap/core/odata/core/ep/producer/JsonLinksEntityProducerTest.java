package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
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
public class JsonLinksEntityProducerTest extends BaseTest {
  protected static final String BASE_URI = "http://host:80/service/";
  protected static final EntityProviderWriteProperties DEFAULT_PROPERTIES =
      EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI)).build();

  @Test
  public void serializeEmployeeLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    Map<String, Object> employeeData = new HashMap<String, Object>();
    employeeData.put("EmployeeId", "1");
    ArrayList<Map<String, Object>> employeesData = new ArrayList<Map<String, Object>>();
    employeesData.add(employeeData);

    final ODataResponse response = new JsonEntityProvider().writeLinks(entitySet, employeesData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":[{\"uri\":\"" + BASE_URI + "Employees('1')\"}]}", json);
  }

  @Test
  public void serializeEmployeeLinks() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    Map<String, Object> employee1 = new HashMap<String, Object>();
    employee1.put("EmployeeId", "1");
    Map<String, Object> employee2 = new HashMap<String, Object>();
    employee2.put("EmployeeId", "2");
    Map<String, Object> employee3 = new HashMap<String, Object>();
    employee3.put("EmployeeId", "3");
    ArrayList<Map<String, Object>> employeesData = new ArrayList<Map<String, Object>>();
    employeesData.add(employee1);
    employeesData.add(employee2);
    employeesData.add(employee3);

    final ODataResponse response = new JsonEntityProvider().writeLinks(entitySet, employeesData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":[{\"uri\":\"" + BASE_URI + "Employees('1')\"},"
        + "{\"uri\":\"" + BASE_URI + "Employees('2')\"},"
        + "{\"uri\":\"" + BASE_URI + "Employees('3')\"}]}",
        json);
  }

  @Test
  public void serializeEmptyList() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

    final ODataResponse response = new JsonEntityProvider().writeLinks(entitySet, data, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":[]}", json);
  }

  @Test
  public void serializeLinksAndInlineCount() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    Map<String, Object> employee1 = new HashMap<String, Object>();
    employee1.put("EmployeeId", "1");
    Map<String, Object> employee2 = new HashMap<String, Object>();
    employee2.put("EmployeeId", "2");
    ArrayList<Map<String, Object>> employeesData = new ArrayList<Map<String, Object>>();
    employeesData.add(employee1);
    employeesData.add(employee2);

    final ODataResponse response = new JsonEntityProvider().writeLinks(entitySet, employeesData,
        EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI))
            .inlineCountType(InlineCount.ALLPAGES).inlineCount(42).build());
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__count\":\"42\",\"results\":["
        + "{\"uri\":\"" + BASE_URI + "Employees('1')\"},"
        + "{\"uri\":\"" + BASE_URI + "Employees('2')\"}]}}",
        json);
  }
}
