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
package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class ProviderFacadeImplTest {

  public static final String EMPLOYEE_1_XML =
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
  public void testReadEntry() throws Exception {
    ProviderFacadeImpl provider = new ProviderFacadeImpl();

    String contentType = ContentType.APPLICATION_ATOM_XML_ENTRY.toContentTypeString();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    Object result = provider.readEntry(contentType, entitySet, content, EntityProviderReadProperties.init().mergeSemantic(true).build());

    assertTrue(result instanceof ODataEntry);
  }

  @Test
  public void testReadPropertyValue() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EntryDate");
    InputStream content = new ByteArrayInputStream("2012-02-29T01:02:03".getBytes("UTF-8"));
    final Object result = new ProviderFacadeImpl().readPropertyValue(property, content, Long.class);
    assertEquals(1330477323000L, result);
  }

  @Test
  @Ignore
  public void testWriteServiceDocument() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWritePropertyValue() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteText() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteBinary() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testReadLink() {
    fail("Not yet implemented");
  }

  @Test
  public void testReadProperty() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    final String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">42</Age>";
    InputStream content = new ByteArrayInputStream(xml.getBytes("UTF-8"));
    final Map<String, Object> result = new ProviderFacadeImpl().readProperty(HttpContentType.APPLICATION_XML, property, content, EntityProviderReadProperties.init().build());
    assertFalse(result.isEmpty());
    assertEquals(42, result.get("Age"));
  }

  @Test
  @Ignore
  public void testReadLinks() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteFeed() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteEntry() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteProperty() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteLink() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteLinks() {
    fail("Not yet implemented");
  }

  @Test
  @Ignore
  public void testWriteFunctionImport() {
    fail("Not yet implemented");
  }

}
