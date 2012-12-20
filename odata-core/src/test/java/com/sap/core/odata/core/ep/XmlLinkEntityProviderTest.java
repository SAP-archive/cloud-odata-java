package com.sap.core.odata.core.ep;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.MockFacade;

/**
 * @author SAP AG
 */
public class XmlLinkEntityProviderTest extends AbstractProviderTest {

  @Test
  public void serializeEmployeeLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");

    final ODataEntityContent content = createAtomEntityProvider().writeLink(entitySet, employeeData, DEFAULT_PROPERTIES);
    assertNotNull(content);
    assertNotNull(content.getContent());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", content.getContentHeader());

    final String xml = StringHelper.inputStreamToString(content.getContent());
    assertNotNull(xml);

    assertXpathExists("/d:uri", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Employees('1')", "/d:uri/text()", xml);
  }

  @Test
  public void serializePhotoLink() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");

    final ODataEntityContent content = createAtomEntityProvider().writeLink(entitySet, photoData, DEFAULT_PROPERTIES);
    assertNotNull(content);
    assertNotNull(content.getContent());

    final String xml = StringHelper.inputStreamToString(content.getContent());
    assertNotNull(xml);

    assertXpathExists("/d:uri", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Container2.Photos(Id=1,Type='JPG')", "/d:uri/text()", xml);
  }
}
