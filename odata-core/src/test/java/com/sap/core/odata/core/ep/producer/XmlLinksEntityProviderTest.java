package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class XmlLinksEntityProviderTest extends AbstractProviderTest {

  @Test
  public void serializeRoomLinks() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    initializeRoomData(2);

    final ODataResponse response = createAtomEntityProvider().writeLinks(entitySet, roomsData, DEFAULT_PROPERTIES);
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_XML.toString() + "; charset=utf-8", response.getContentHeader());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:links", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Rooms('1')", "/d:links/d:uri/text()", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Rooms('2')", "/d:links/d:uri[2]/text()", xml);
  }

  @Test
  public void linksWithInlineCount() throws Exception {
    final EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    initializeRoomData(1);

    final ODataResponse response = createAtomEntityProvider().writeLinks(entitySet, roomsData,
        EntityProviderProperties.baseUri(BASE_URI).inlineCount(3).build());
    assertNotNull(response);
    assertNotNull(response.getEntity());

    final String xml = StringHelper.inputStreamToString((InputStream) response.getEntity());
    assertNotNull(xml);

    assertXpathExists("/d:links", xml);
    assertXpathEvaluatesTo("3", "/d:links/m:count/text()", xml);
    assertXpathEvaluatesTo(BASE_URI.toString() + "Rooms('1')", "/d:links/d:uri/text()", xml);
  }
}
