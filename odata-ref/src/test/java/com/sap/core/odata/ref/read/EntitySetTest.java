/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.ref.read;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.ref.model.DataContainer;
import com.sap.core.odata.ref.processor.ListsProcessor;
import com.sap.core.odata.ref.processor.ScenarioDataSource;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EntitySetTest extends BaseTest {

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

  @Before
  public void setUp() throws Exception {
    ODataContext context = mock(ODataContext.class);
    PathInfo uriInfo = mock(PathInfo.class);
    when(uriInfo.getServiceRoot()).thenReturn(new URI("http://localhost/"));
    when(context.getPathInfo()).thenReturn(uriInfo);

    processor.setContext(context);
  }

  private UriInfo mockUriResult(final String entitySetName) throws ODataException, URISyntaxException {
    EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getName()).thenReturn(entitySetName);
    EdmEntityContainer entityContainer = mock(EdmEntityContainer.class);
    when(entityContainer.isDefaultEntityContainer()).thenReturn(true);
    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(entitySetName);
    when(entitySet.getEntityType()).thenReturn(entityType);
    when(entitySet.getEntityContainer()).thenReturn(entityContainer);

    UriInfo uriResult = mock(UriInfo.class);
    when(uriResult.getStartEntitySet()).thenReturn(entitySet);
    when(uriResult.getTargetEntitySet()).thenReturn(entitySet);
    when(uriResult.getTop()).thenReturn(null);
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
    final UriInfo uriResult = mockUriResult("Employees");

    ODataResponse response = processor.readEntitySet(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Employee"));
  }

  @Test
  public void readTeams() throws Exception {
    final UriInfo uriResult = mockUriResult("Teams");

    ODataResponse response = processor.readEntitySet(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Team"));
  }

  @Test
  public void readRooms() throws Exception {
    final UriInfo uriResult = mockUriResult("Rooms");

    ODataResponse response = processor.readEntitySet(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Room"));
  }

  @Test
  public void readManagers() throws Exception {
    final UriInfo uriResult = mockUriResult("Managers");

    ODataResponse response = processor.readEntitySet(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Manager"));
  }

  @Test
  public void readBuildings() throws Exception {
    final UriInfo uriResult = mockUriResult("Buildings");

    ODataResponse response = processor.readEntitySet(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Building"));
  }

  @Test
  public void readPhotos() throws Exception {
    final UriInfo uriResult = mockUriResult("Photos");

    ODataResponse response = processor.readEntitySet(uriResult, ContentType.APPLICATION_ATOM_XML_FEED.toContentTypeString());
    assertNotNull(response);
    assertTrue(readContent(response).contains("Photo"));
  }

}
