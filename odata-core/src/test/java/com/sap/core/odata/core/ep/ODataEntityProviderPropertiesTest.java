package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.Test;

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.ep.EntityProviderWriteProperties;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataEntityProviderPropertiesTest extends BaseTest {

  @Test
  public void buildFeedProperties() throws Exception {
    URI serviceRoot = new URI("http://localhost:80/");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
        .inlineCountType(InlineCount.ALLPAGES)
        .inlineCount(1)
        .nextLink("http://localhost")
        .build();

    assertEquals("Wrong base uri.", "http://localhost:80/", properties.getServiceRoot().toASCIIString());
    assertEquals("Wrong inline count type.", InlineCount.ALLPAGES, properties.getInlineCountType());
    assertEquals("Wrong inline count.", Integer.valueOf(1), properties.getInlineCount());
    assertEquals("Wrong nextLink", "http://localhost", properties.getNextLink());
  }

  @Test
  public void buildPropertiesDefaults() throws Exception {
    URI baseUri = new URI("http://localhost:80/");
    EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(baseUri).build();

    assertEquals("http://localhost:80/", properties.getServiceRoot().toASCIIString());
    assertNull(properties.getInlineCountType());
    assertNull(properties.getInlineCount());
    assertNull(properties.getMediaResourceMimeType());
    assertNull(properties.getNextLink());
  }

  @Test
  public void buildEntryProperties() throws Exception {
    final String mediaResourceMimeType = "text/html";
    final URI serviceRoot = new URI("http://localhost:80/");
    final EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
        .mediaResourceMimeType(mediaResourceMimeType)
        .build();
    assertEquals("Wrong mime type.", "text/html", properties.getMediaResourceMimeType());
  }
}