package com.sap.core.odata.ref.read.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.CollectionsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;

public class EntitySetTest {

  private static DataContainer dataContainer;
  private static ScenarioDataSource dataSource;
  private static CollectionsProcessor processor;

  @BeforeClass
  public static void init() {
    dataContainer = new DataContainer();
    dataContainer.reset();
    dataSource = new ScenarioDataSource(dataContainer);
    processor = new CollectionsProcessor(dataSource);
  }

  @Test
  public void readEmployees() throws Exception {
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Employees");

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Employee"));
  }

  @Test
  public void readTeams() throws Exception {
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Teams");

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Team"));
  }

  @Test
  public void readRooms() throws Exception {
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Rooms");

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Room"));
  }

  @Test
  public void readManagers() throws Exception {
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Managers");

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Manager"));
  }

  @Test
  public void readBuildings() throws Exception {
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn("Buildings");

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);

    ODataResponse response = processor.readEntitySet(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().contains("Building"));
  }

}
