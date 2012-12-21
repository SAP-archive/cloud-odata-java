package com.sap.core.odata.core.ep;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.enums.ContentType;
import com.sap.core.odata.api.ep.ODataEntityContent;
import com.sap.core.odata.api.ep.ODataEntityProviderProperties;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.MockFacade;

/**
 * @author SAP AG
 */
public class XmlLinksEntityProviderTest extends AbstractProviderTest {

  @Test
  public void serializeRoomLinks() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    initializeRoomData(2);

    final ODataEntityContent content = createAtomEntityProvider().writeLinks(entitySet, roomsData, DEFAULT_PROPERTIES);
    assertNotNull(content);
    assertNotNull(content.getContent());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", content.getContentHeader());

    final String xml = StringHelper.inputStreamToString(content.getContent());
    assertNotNull(xml);

    assertXpathExists("/d:links", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Rooms('1')", "/d:links/d:uri/text()", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Rooms('2')", "/d:links/d:uri[2]/text()", xml);
  }

  @Test
  public void linksWithInlineCount() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    initializeRoomData(1);

    final ODataEntityContent content = createAtomEntityProvider().writeLinks(entitySet, roomsData,
        ODataEntityProviderProperties.baseUri(BASE_URI).inlineCount(3).build());
    assertNotNull(content);
    assertNotNull(content.getContent());

    final String xml = StringHelper.inputStreamToString(content.getContent());
    assertNotNull(xml);

    assertXpathExists("/d:links", xml);
    assertXpathEvaluatesTo("3", "/d:links/m:count/text()", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Rooms('1')", "/d:links/d:uri/text()", xml);
  }
}
