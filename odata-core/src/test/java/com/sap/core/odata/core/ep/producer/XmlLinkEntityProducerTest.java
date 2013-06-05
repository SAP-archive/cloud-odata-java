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

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class XmlLinkEntityProducerTest extends AbstractProviderTest {

  public XmlLinkEntityProducerTest(final StreamWriterImplType type) {
    super(type);
  }

  @Test
  public void serializeEmployeeLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");

    final ODataResponse content = createAtomEntityProvider().writeLink(entitySet, employeeData, DEFAULT_PROPERTIES);
    assertNotNull(content);
    assertNotNull(content.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", content.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) content.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:uri", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Employees('1')", "/d:uri/text()", xml);
  }

  @Test
  public void serializePhotoLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");

    final ODataResponse response = createAtomEntityProvider().writeLink(entitySet, photoData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:uri", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Container2.Photos(Id=1,Type='image%2Fpng')", "/d:uri/text()", xml);
  }
}
