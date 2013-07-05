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
package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.helper.XMLUnitHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class XmlPropertyProducerTest extends AbstractProviderTest {

  public XmlPropertyProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  @Test
  public void serializeEmployeeId() throws Exception {
    AtomEntityProvider s = createAtomEntityProvider();
    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeId");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    ODataResponse response = s.writeProperty(edmProperty, employeeData.get("EmployeeId"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + ";charset=utf-8", response.getContentHeader());

    String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:EmployeeId", xml);
    assertXpathEvaluatesTo("1", "/d:EmployeeId/text()", xml);
  }

  @Test
  public void serializeAge() throws Exception {
    AtomEntityProvider s = createAtomEntityProvider();

    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    ODataResponse response = s.writeProperty(edmProperty, employeeData.get("Age"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + ";charset=utf-8", response.getContentHeader());
    String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:Age", xml);
    assertXpathEvaluatesTo("52", "/d:Age/text()", xml);
  }

  @Test
  public void serializeImageUrl() throws Exception {
    AtomEntityProvider s = createAtomEntityProvider();

    EdmTyped edmTyped = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("ImageUrl");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    ODataResponse response = s.writeProperty(edmProperty, employeeData.get("ImageUrl"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + ";charset=utf-8", response.getContentHeader());
    String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:ImageUrl", xml);
    assertXpathExists("/d:ImageUrl/@m:null", xml);
    assertXpathEvaluatesTo("true", "/d:ImageUrl/@m:null", xml);
  }

  @Test
  public void serializeImage() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario2", "Photo").getProperty("Image");
    ODataResponse response = createAtomEntityProvider().writeProperty(property, photoData.get("Image"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);
    assertXpathExists("/d:Image", xml);
    assertXpathExists("/d:Image/@m:MimeType", xml);
    assertXpathEvaluatesTo("image/png", "/d:Image/@m:MimeType", xml);
  }

  @Test
  public void serializeBinaryData() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario2", "Photo").getProperty("BinaryData");
    ODataResponse response = createAtomEntityProvider().writeProperty(property, photoData.get("BinaryData"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(HttpContentType.APPLICATION_XML_UTF8, response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);
    assertXpathExists("/d:BinaryData", xml);
    assertXpathExists("/d:BinaryData/@m:MimeType", xml);
    assertXpathEvaluatesTo("image/jpeg", "/d:BinaryData/@m:MimeType", xml);
  }

  @Test
  public void serializeLocation() throws Exception {
    AtomEntityProvider s = createAtomEntityProvider();

    EdmEntityType edmEntityType = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee");
    EdmTyped edmTyped = edmEntityType.getProperty("Location");
    EdmProperty edmProperty = (EdmProperty) edmTyped;

    ODataResponse response = s.writeProperty(edmProperty, employeeData.get("Location"));
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + ";charset=utf-8", response.getContentHeader());
    String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:Location", xml);
    assertXpathExists("/d:Location/d:City", xml);
    assertXpathExists("/d:Location/d:City/d:PostalCode", xml);
    assertXpathExists("/d:Location/d:City/d:CityName", xml);
    assertXpathExists("/d:Location/d:Country", xml);

    // verify order of tags
    // first outer tags (city/country)
    XMLUnitHelper.verifyTagOrdering(xml, "City", "Country");
    // then inner tags (postalcode/cityname)
    XMLUnitHelper.verifyTagOrdering(xml, "PostalCode", "CityName");

    assertXpathEvaluatesTo("RefScenario.c_Location", "/d:Location/@m:type", xml);

    assertXpathEvaluatesTo("33470", "/d:Location/d:City/d:PostalCode/text()", xml);
    assertXpathEvaluatesTo("Duckburg", "/d:Location/d:City/d:CityName/text()", xml);
    assertXpathEvaluatesTo("Calisota", "/d:Location/d:Country/text()", xml);
  }

}
