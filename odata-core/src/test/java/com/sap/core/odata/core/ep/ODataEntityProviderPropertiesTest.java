package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.Test;

import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataEntityProviderPropertiesTest extends BaseTest {

  @Test
  public void buildFeedProperties() throws Exception {
    String mediaResourceMimeType = "text/html";
    URI serviceRoot = new URI("http://localhost:80/");
    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(serviceRoot)
        .mediaResourceMimeType(mediaResourceMimeType)
        .inlineCount(-1)
        .nextLink("http://localhost")
        .build();

    assertEquals("Wrong base uri.", "http://localhost:80/", properties.getServiceRoot().toASCIIString());
    assertEquals("Wrong mime type.", "text/html", properties.getMediaResourceMimeType());
    assertEquals("Wrong inline count.", Integer.valueOf(-1), properties.getInlineCount());
    assertEquals("Wrong nextLink", "http://localhost", properties.getNextLink());
  }

  @Test
  public void buildFeedPropertiesDefaults() throws Exception {
    URI baseUri = new URI("http://localhost:80/");
    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(baseUri).build();

    assertEquals("http://localhost:80/", properties.getServiceRoot().toASCIIString());
    assertNull(properties.getInlineCount());
    assertNull(properties.getMediaResourceMimeType());
    assertNull(properties.getNextLink());
  }
}