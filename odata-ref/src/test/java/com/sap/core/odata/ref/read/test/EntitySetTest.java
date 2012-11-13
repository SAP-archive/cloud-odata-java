package com.sap.core.odata.ref.read.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;

/**
 * @author SAP AG
 */
public class EntitySetTest {

  private static DataContainer dataContainer;
  private static ScenarioDataSource dataSource;
  private static ListsProcessor processor;

  @BeforeClass
  public static void init() {
    dataContainer = new DataContainer();
    dataContainer.reset();
    dataSource = new ScenarioDataSource(dataContainer);
    processor = new ListsProcessor(dataSource);
  }

  private UriParserResult mockUriResult(final String entitySetName) throws EdmException {
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(entitySetName);

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);
    when(uriResult.getTop()).thenReturn(null);
    return uriResult;
  }

  @Test
  public void readEmployees() throws Exception {
    final UriParserResult uriResult = mockUriResult("Employees");

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Employee"));
  }

  @Test
  public void readTeams() throws Exception {
    final UriParserResult uriResult = mockUriResult("Teams");

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Team"));
  }

  @Test
  public void readRooms() throws Exception {
    final UriParserResult uriResult = mockUriResult("Rooms");

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Room"));
  }

  @Test
  public void readManagers() throws Exception {
    final UriParserResult uriResult = mockUriResult("Managers");

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Manager"));
  }

  @Test
  public void readBuildings() throws Exception {
    final UriParserResult uriResult = mockUriResult("Buildings");

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Building"));
  }
  
  @Test
  public void readPhotos() throws Exception {
    final UriParserResult uriResult = mockUriResult("Photos");

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Photo"));
  }


}
