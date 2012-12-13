package com.sap.core.odata.core.ep;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URI;

import org.junit.Test;

import com.sap.core.odata.api.ep.ODataEntityProviderProperties;

public class ODataEntityProviderPropertiesTest {

  @Test
  public void buildFeedProperties() throws Exception {
    String mediaResourceMimeType = "text/html";
    URI baseUri = new URI("http://localhost:80/");
    ODataEntityProviderProperties properties = ODataEntityProviderProperties.baseUri(baseUri)
        .mediaResourceMimeType(mediaResourceMimeType)
        .inlineCount(-1)
        .skipToken("UUID=A823K34WER3@#$20")
        .build();
    
    assertEquals("Wrong base uri.", "http://localhost:80/", properties.getBaseUri().toASCIIString());
    assertEquals("Wrong mime type.", "text/html", properties.getMediaResourceMimeType());
    assertEquals("Wrong inline count.", Integer.valueOf(-1), properties.getInlineCount());
    assertEquals("Wrong skiptoken", "UUID=A823K34WER3@#$20", properties.getSkipToken());
  }
  
  @Test
  public void buildFeedPropertiesDefaults() throws Exception {
    URI baseUri = new URI("http://localhost:80/");
    ODataEntityProviderProperties properties = ODataEntityProviderProperties.baseUri(baseUri).build();
    
    assertEquals("http://localhost:80/", properties.getBaseUri().toASCIIString());
    assertNull(properties.getInlineCount());
    assertNull(properties.getMediaResourceMimeType());
    assertNull(properties.getSkipToken());
  }
}