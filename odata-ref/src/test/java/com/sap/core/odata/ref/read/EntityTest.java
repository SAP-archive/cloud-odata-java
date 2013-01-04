package com.sap.core.odata.ref.read;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.core.enums.ContentType;
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

  private ODataContext mockedContext;

  @BeforeClass
  public static void init() {
    dataContainer = new DataContainer();
    dataContainer.reset();
    dataSource = new ScenarioDataSource(dataContainer);
    processor = new ListsProcessor(dataSource);
  }

  @Before
  public void setUp() throws Exception {
    mockedContext = mock(ODataContext.class);
    UriInfo uriInfo = mock(UriInfo.class);
    when(uriInfo.getBaseUri()).thenReturn(new URI("http://localhost/"));
    when(mockedContext.getUriInfo()).thenReturn(uriInfo);

    processor.setContext(mockedContext);
  }

  private UriParserResult mockUriResult(final String entitySetName, final String keyName, final String keyValue) throws EdmException {
    EdmProperty keyProperty = mock(EdmProperty.class);
    when(keyProperty.getName()).thenReturn(keyName);
    when(keyProperty.getType()).thenReturn(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    EdmMapping mapping = mock(EdmMapping.class);
    when(mapping.getInternalName()).thenReturn("getId");
    when(keyProperty.getMapping()).thenReturn(mapping);

    KeyPredicate key = mock(KeyPredicate.class);
    when(key.getProperty()).thenReturn(keyProperty);
    when(key.getLiteral()).thenReturn(keyValue);

    ArrayList<KeyPredicate> keys = new ArrayList<KeyPredicate>();
    keys.add(key);

    EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getName()).thenReturn(entitySetName);
    when(entityType.getProperty(keyProperty.getName())).thenReturn(keyProperty);
    when(entityType.getPropertyNames()).thenReturn(Arrays.asList(keyName));

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(entitySetName);
    when(entitySet.getEntityType()).thenReturn(entityType);

    EdmEntityContainer entityContainer = mock(EdmEntityContainer.class);
    when(entityContainer.isDefaultEntityContainer()).thenReturn(true);
    when(entitySet.getEntityContainer()).thenReturn(entityContainer);

    UriParserResult uriResult = mock(UriParserResult.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
    when(uriResult.getKeyPredicates()).thenReturn(keys);
    return uriResult;
  }

  private String readContent(final ODataResponse response) throws IOException {
    CharBuffer content = CharBuffer.allocate(1000);
    new InputStreamReader((InputStream) response.getEntity()).read(content);
    content.rewind();
    return content.toString();
  }

  @Test
  public void readEmployees() throws Exception {
    final UriParserResult uriResult = mockUriResult("Employees", "EmployeeId", "5");

    ODataResponse response = processor.readEntity(uriResult, ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Employee"));
  }

  @Test
  public void readTeams() throws Exception {
    final UriParserResult uriResult = mockUriResult("Teams", "Id", "1");

    ODataResponse response = processor.readEntity(uriResult, ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Team"));
  }

  @Test
  public void readRooms() throws Exception {
    final UriParserResult uriResult = mockUriResult("Rooms", "Id", "1");

    ODataResponse response = processor.readEntity(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Room"));
  }

  @Test
  public void readManagers() throws Exception {
    final UriParserResult uriResult = mockUriResult("Managers", "EmployeeId", "1");

    ODataResponse response = processor.readEntity(uriResult, ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Manager"));
  }

  @Test
  public void readBuildings() throws Exception {
    final UriParserResult uriResult = mockUriResult("Buildings", "Id", "1");

    ODataResponse response = processor.readEntity(uriResult, ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Building"));
  }

}
