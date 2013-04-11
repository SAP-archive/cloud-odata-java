package com.sap.core.odata.core.ep.producer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.api.uri.SelectItem;
import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.core.ep.JsonEntityProvider;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class JsonEntryEntityProducerTest extends BaseTest {
  protected static final String BASE_URI = "http://host:80/service/";
  protected static final EntityProviderWriteProperties DEFAULT_PROPERTIES =
      EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI)).build();

  @Test
  public void entry() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    Map<String, Object> teamData = new HashMap<String, Object>();
    teamData.put("Id", "1");
    teamData.put("isScrumTeam", true);

    final ODataResponse response = new JsonEntityProvider().writeEntry(entitySet, teamData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + BASE_URI + "Teams('1')\","
        + "\"uri\":\"" + BASE_URI + "Teams('1')\",\"type\":\"RefScenario.Team\"},"
        + "\"Id\":\"1\",\"Name\":null,\"isScrumTeam\":true,"
        + "\"nt_Employees\":{\"__deferred\":{\"uri\":\"" + BASE_URI + "Teams('1')/nt_Employees\"}}}}",
        json);
  }

  @Test
  public void entryWithSelect() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Buildings");
    Map<String, Object> buildingData = new HashMap<String, Object>();
    buildingData.put("Id", "1");

    final EdmNavigationProperty property = (EdmNavigationProperty) entitySet.getEntityType().getProperty("nb_Rooms");
    NavigationPropertySegment segment = Mockito.mock(NavigationPropertySegment.class);
    Mockito.when(segment.getNavigationProperty()).thenReturn(property);
    SelectItem selectItem = Mockito.mock(SelectItem.class);
    Mockito.when(selectItem.getNavigationPropertySegments()).thenReturn(Arrays.asList(segment));
    final ODataResponse response = new JsonEntityProvider().writeEntry(entitySet, buildingData,
        EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI))
            .expandSelectTree(UriParser.createExpandSelectTree(Arrays.asList(selectItem), null))
            .build());
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{\"id\":\"" + BASE_URI + "Buildings('1')\","
        + "\"uri\":\"" + BASE_URI + "Buildings('1')\",\"type\":\"RefScenario.Building\"},"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + BASE_URI + "Buildings('1')/nb_Rooms\"}}}}",
        json);
  }

  @Test
  public void mediaLinkEntry() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");
    Map<String, Object> photoData = new HashMap<String, Object>();
    photoData.put("Id", 1);
    photoData.put("Type", "image/png");
    photoData.put("BinaryData", new byte[] { -1, 0, 1, 2 });
    photoData.put("getType", "image/png");

    final ODataResponse response = new JsonEntityProvider().writeEntry(entitySet, photoData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{"
        + "\"id\":\"" + BASE_URI + "Container2.Photos(Id=1,Type='image%2fpng')\","
        + "\"uri\":\"" + BASE_URI + "Container2.Photos(Id=1,Type='image%2fpng')\","
        + "\"type\":\"RefScenario2.Photo\",\"etag\":\"W/\\\"1\\\"\",\"content_type\":\"image/png\","
        + "\"media_src\":\"Container2.Photos(Id=1,Type='image%2fpng')/$value\","
        + "\"edit_media\":\"" + BASE_URI + "Container2.Photos(Id=1,Type='image%2fpng')/$value\"},"
        + "\"Id\":1,\"Name\":null,\"Type\":\"image/png\",\"Image\":null,"
        + "\"BinaryData\":\"/wABAg==\",\"Содержание\":null,\"CustomProperty\":null}}",
        json);
  }

  @Test
  public void mediaLinkEntryWithSelect() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    Map<String, Object> employeeData = new HashMap<String, Object>();
    employeeData.put("EmployeeId", "1");
    employeeData.put("EntryDate", 0L);
    employeeData.put("getImageType", "image/jpeg");

    final EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EntryDate");
    SelectItem selectItem = Mockito.mock(SelectItem.class);
    Mockito.when(selectItem.getProperty()).thenReturn(property);
    final ODataResponse response = new JsonEntityProvider().writeEntry(entitySet, employeeData,
        EntityProviderWriteProperties.serviceRoot(URI.create(BASE_URI))
            .expandSelectTree(UriParser.createExpandSelectTree(Arrays.asList(selectItem), null))
            .build());
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_JSON, response.getContentHeader());

    final String json = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(json);
    assertEquals("{\"d\":{\"__metadata\":{"
        + "\"id\":\"" + BASE_URI + "Employees('1')\","
        + "\"uri\":\"" + BASE_URI + "Employees('1')\","
        + "\"type\":\"RefScenario.Employee\",\"content_type\":\"image/jpeg\","
        + "\"media_src\":\"Employees('1')/$value\","
        + "\"edit_media\":\"" + BASE_URI + "Employees('1')/$value\"},"
        + "\"EntryDate\":\"\\/Date(0)\\/\"}}",
        json);
  }
}
