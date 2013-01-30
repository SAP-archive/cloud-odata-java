package com.sap.core.odata.core.commons;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

public class EncoderTest extends BaseTest {

  @Test
  public void testAsciiCharacters() {
    String s = "azAZ0123456789";
    assertEquals(s, Encoder.encodePart(s));
    assertEquals(s, Encoder.encodePart(s));
  }

  @Test
  public void testUnsafeCharacters() {
    String s = " #$%&+,/:;=?@[]<>";
    assertEquals("%20%23%24%25%26%2B%2C%2F%3A%3B%3D%3F%40%5B%5D%3C%3E".toLowerCase(), Encoder.encodePart(s));
  }

  @Test
  public void testSafeCharacters() {
    String s = "_-.~";
    assertEquals(s, Encoder.encodePart(s));
    assertEquals(s, Encoder.encodePart(s));
  }

  @Test
  public void testUnicodeCharacters() {
    String s = "€";
    assertEquals("%E2%82%AC".toLowerCase(), Encoder.encodePart(s));
  }

  @Test
  public void uriDecoding() throws URISyntaxException {
  String decodedValue = "><€ -._~:/?#[]@!$&'()*+,;=%";

    String encodedPath = Encoder.encodePart(decodedValue) + "/" + Encoder.encodePart(decodedValue);
    String encodedQuery = Encoder.encodePart(decodedValue);
    URI uri = new URI("http://host:80/" + encodedPath + "?" + encodedQuery + "=" + encodedQuery);

    assertEquals(uri.getPath(), "/" + decodedValue + "/" + decodedValue);
    assertEquals(uri.getQuery(), decodedValue + "=" + decodedValue);

    assertEquals(uri.getRawPath(), "/" + encodedPath);
    assertEquals(uri.getRawQuery(), encodedQuery + "=" + encodedQuery);
  }

}
