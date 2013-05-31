package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class ProviderFacadeImplTest {

  private static final String EMPLOYEE_1_XML =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<entry xmlns=\"" + Edm.NAMESPACE_ATOM_2005 + "\"" +
          " xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\"" +
          " xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\"" +
          " xml:base=\"https://some.host.com/some.service.root.segment/ReferenceScenario.svc/\">" +
          "<id>https://some.host.com/some.service.root.segment/ReferenceScenario.svc/Employees('1')</id>" +
          "<title type=\"text\">Walter Winter</title>" +
          "<updated>1999-01-01T00:00:00Z</updated>" +
          "<category term=\"RefScenario.Employee\" scheme=\"" + Edm.NAMESPACE_SCHEME_2007_08 + "\"/>" +
          "<link href=\"Employees('1')\" rel=\"edit\" title=\"Employee\"/>" +
          "<link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\"/>" +
          "<link href=\"Employees('1')/ne_Room\" rel=\"" + Edm.NAMESPACE_REL_2007_08 + "ne_Room\" type=\"application/atom+xml; type=entry\" title=\"ne_Room\"/>" +
          "<link href=\"Employees('1')/ne_Manager\" rel=\"" + Edm.NAMESPACE_REL_2007_08 + "ne_Manager\" type=\"application/atom+xml; type=entry\" title=\"ne_Manager\"/>" +
          "<link href=\"Employees('1')/ne_Team\" rel=\"" + Edm.NAMESPACE_REL_2007_08 + "ne_Team\" type=\"application/atom+xml; type=entry\" title=\"ne_Team\"/>" +
          "<content type=\"application/octet-stream\" src=\"Employees('1')/$value\"/>" +
          "<m:properties>" +
          "<d:EmployeeId>1</d:EmployeeId>" +
          "<d:EmployeeName>Walter Winter</d:EmployeeName>" +
          "<d:ManagerId>1</d:ManagerId>" +
          "<d:RoomId>1</d:RoomId>" +
          "<d:TeamId>1</d:TeamId>" +
          "<d:Location m:type=\"RefScenario.c_Location\">" +
          "<d:Country>Germany</d:Country>" +
          "<d:City m:type=\"RefScenario.c_City\">" +
          "<d:PostalCode>69124</d:PostalCode>" +
          "<d:CityName>Heidelberg</d:CityName>" +
          "</d:City>" +
          "</d:Location>" +
          "<d:Age>52</d:Age>" +
          "<d:EntryDate>1999-01-01T00:00:00</d:EntryDate>" +
          "<d:ImageUrl>male_1_WinterW.jpg</d:ImageUrl>" +
          "</m:properties>" +
          "</entry>";

  @Test
  public void readEntry() throws Exception {
    final String contentType = ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString();
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("UTF-8"));

    final ODataEntry result = new ProviderFacadeImpl().readEntry(contentType, entitySet, content, EntityProviderReadProperties.init().mergeSemantic(true).build());
    assertNotNull(result);
    assertFalse(result.containsInlineEntry());
    assertNotNull(result.getExpandSelectTree());
    assertTrue(result.getExpandSelectTree().isAll());
    assertNotNull(result.getMetadata());
    assertNull(result.getMetadata().getEtag());
    assertNotNull(result.getMediaMetadata());
    assertEquals(HttpContentType.APPLICATION_OCTET_STREAM, result.getMediaMetadata().getContentType());
    assertNotNull(result.getProperties());
    assertEquals(52, result.getProperties().get("Age"));
  }

  @Test
  public void readPropertyValue() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");
    InputStream content = new ByteArrayInputStream("2012-02-29T01:02:03".getBytes("UTF-8"));
    final Object result = new ProviderFacadeImpl().readPropertyValue(property, content, Long.class);
    assertEquals(1330477323000L, result);
  }

  @Test
  public void readProperty() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    final String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">42</Age>";
    InputStream content = new ByteArrayInputStream(xml.getBytes("UTF-8"));
    final Map<String, Object> result = new ProviderFacadeImpl().readProperty(HttpContentType.APPLICATION_XML, property, content, EntityProviderReadProperties.init().build());
    assertFalse(result.isEmpty());
    assertEquals(42, result.get("Age"));
  }

  @Test
  public void readLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream content = new ByteArrayInputStream("{\"d\":{\"uri\":\"http://somelink\"}}".getBytes("UTF-8"));
    final String result = new ProviderFacadeImpl().readLink(HttpContentType.APPLICATION_JSON, entitySet, content);
    assertEquals("http://somelink", result);
  }

  @Test
  public void readLinks() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream content = new ByteArrayInputStream("{\"d\":{\"__count\":\"42\",\"results\":[{\"uri\":\"http://somelink\"}]}}".getBytes("UTF-8"));
    final List<String> result = new ProviderFacadeImpl().readLinks(HttpContentType.APPLICATION_JSON, entitySet, content);
    assertEquals(Arrays.asList("http://somelink"), result);
  }

  @Test
  public void writeFeed() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    List<Map<String, Object>> propertiesList = new ArrayList<Map<String, Object>>();
    final ODataResponse result = new ProviderFacadeImpl().writeFeed(HttpContentType.APPLICATION_JSON, entitySet, propertiesList, EntityProviderWriteProperties.serviceRoot(URI.create("http://root/")).build());
    assertEquals("{\"d\":{\"results\":[]}}", StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writeEntry() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("Id", "42");
    final ODataResponse result = new ProviderFacadeImpl().writeEntry(HttpContentType.APPLICATION_JSON, entitySet, properties, EntityProviderWriteProperties.serviceRoot(URI.create("http://root/")).build());
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"http://root/Teams('42')\","
        + "\"uri\":\"http://root/Teams('42')\",\"type\":\"RefScenario.Team\"},"
        + "\"Id\":\"42\",\"Name\":null,\"isScrumTeam\":null,"
        + "\"nt_Employees\":{\"__deferred\":{\"uri\":\"http://root/Teams('42')/nt_Employees\"}}}}",
        StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writeProperty() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");
    final ODataResponse result = new ProviderFacadeImpl().writeProperty(HttpContentType.APPLICATION_XML, property, 987654321000L);
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, result.getContentHeader());
    assertTrue(StringHelper.inputStreamToString((InputStream) result.getEntity())
        .endsWith("\">2001-04-19T04:25:21</EntryDate>"));
  }

  @Test
  public void writeLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("Id", "42");
    final ODataResponse result = new ProviderFacadeImpl().writeLink(HttpContentType.APPLICATION_JSON, entitySet, properties, EntityProviderWriteProperties.serviceRoot(URI.create("http://root/")).build());
    assertEquals("{\"d\":{\"uri\":\"http://root/Rooms('42')\"}}",
        StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writeLinks() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    Map<String, Object> properties = new HashMap<String, Object>();
    properties.put("Id", "42");
    List<Map<String, Object>> propertiesList = new ArrayList<Map<String, Object>>();
    propertiesList.add(properties);
    propertiesList.add(properties);
    final ODataResponse result = new ProviderFacadeImpl().writeLinks(HttpContentType.APPLICATION_JSON, entitySet, propertiesList, EntityProviderWriteProperties.serviceRoot(URI.create("http://root/")).build());
    assertEquals("{\"d\":[{\"uri\":\"http://root/Rooms('42')\"},{\"uri\":\"http://root/Rooms('42')\"}]}",
        StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writeServiceDocument() throws Exception {
    final ODataResponse result = new ProviderFacadeImpl().writeServiceDocument(HttpContentType.APPLICATION_JSON, MockFacade.getMockEdm(), "root");
    assertEquals("{\"d\":{\"EntitySets\":[]}}", StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writePropertyValue() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");
    final ODataResponse result = new ProviderFacadeImpl().writePropertyValue(property, 987654321000L);
    assertEquals(HttpContentType.TEXT_PLAIN_UTF8, result.getContentHeader());
    assertEquals("2001-04-19T04:25:21", StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writeText() throws Exception {
    final ODataResponse result = new ProviderFacadeImpl().writeText("test");
    assertEquals(HttpContentType.TEXT_PLAIN_UTF8, result.getContentHeader());
    assertEquals("test", StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  public void writeBinary() throws Exception {
    final ODataResponse result = new ProviderFacadeImpl().writeBinary(HttpContentType.APPLICATION_OCTET_STREAM, new byte[] { 102, 111, 111 });
    assertEquals(HttpContentType.APPLICATION_OCTET_STREAM, result.getContentHeader());
    assertEquals("foo", StringHelper.inputStreamToString((InputStream) result.getEntity()));
  }

  @Test
  @Ignore
  public void writeFunctionImport() {
    fail("Not yet implemented");
  }

}
