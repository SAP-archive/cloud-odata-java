package com.sap.core.odata.ref.read.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;

/**
 * @author SAP AG
 */
public class EntityTest {

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

  private UriParserResult mockUriResult(final String entitySetName, final String keyName, final String keyValue) throws EdmException {
    EdmProperty keyProperty = mock(EdmProperty.class);
    when(keyProperty.getName()).thenReturn(keyName);
    when(keyProperty.getType()).thenReturn(EdmSimpleTypeFacade.stringInstance()); 

    KeyPredicate key = mock(KeyPredicate.class);
    when(key.getProperty()).thenReturn(keyProperty);
    when(key.getLiteral()).thenReturn(keyValue);

    ArrayList<KeyPredicate> keys = new ArrayList<KeyPredicate>();
    keys.add(key);

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(entitySetName);

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);
    when(uriResult.getKeyPredicates()).thenReturn(keys);
    return uriResult;
  }

  @Test
  public void readEmployees() throws Exception {
    final UriParserResult uriResult = mockUriResult("Employees", "EmployeeId", "5");

    ODataResponse response = processor.readEntity(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().toString().contains("Employee"));
  }

  @Test
  public void readTeams() throws Exception {
    final UriParserResult uriResult = mockUriResult("Teams", "Id", "1");

    ODataResponse response = processor.readEntity(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().toString().contains("Team"));
  }

  @Test
  public void readRooms() throws Exception {
    final UriParserResult uriResult = mockUriResult("Rooms", "Id", "1");

    ODataResponse response = processor.readEntity(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().toString().contains("Room"));
  }

  @Test
  public void readManagers() throws Exception {
    final UriParserResult uriResult = mockUriResult("Managers", "EmployeeId", "1");

    ODataResponse response = processor.readEntity(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().toString().contains("Manager"));
  }

  @Test
  public void readBuildings() throws Exception {
    final UriParserResult uriResult = mockUriResult("Buildings", "Id", "1");

    ODataResponse response = processor.readEntity(uriResult);
    assertNotNull(response);
    assertTrue(response.getEntity().toString().contains("Building"));
  }

}
