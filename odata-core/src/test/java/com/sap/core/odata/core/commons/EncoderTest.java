package com.sap.core.odata.core.commons;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

public class EncoderTest extends BaseTest {

  private final static String RFC3986_UNRESERVED = "-._~"; // + ALPHA + DIGIT
  private final static String RFC3986_GEN_DELIMS = ":/?#[]@";
  private final static String RFC3986_SUB_DELIMS = "!$&'()*+,;=";
  private final static String RFC3986_RESERVED = RFC3986_GEN_DELIMS + RFC3986_SUB_DELIMS;

  @Test
  public void testAsciiCharacters() {
    String s = "azAZ019";
    assertEquals(s, Encoder.encodePathPart(s));
    assertEquals(s, Encoder.encodePathPart(s));
  }

  @Test
  public void testRfc3986Unreserved() {
    assertEquals(RFC3986_UNRESERVED, Encoder.encodePathPart(RFC3986_UNRESERVED));
  }

  @Test
  public void testRfc3986GenDelims() {
    assertEquals("%3a%2f%3f%23%5b%5d%40".toLowerCase(), Encoder.encodePathPart(RFC3986_GEN_DELIMS));
  }

  @Test
  public void testRfc3986SubDelims() {
    assertEquals(RFC3986_SUB_DELIMS, Encoder.encodePathPart(RFC3986_SUB_DELIMS));
  }

  @Test
  public void testRfc3986Reserved() {
    assertEquals("%3a%2f%3f%23%5b%5d%40!$&'()*+,;=".toLowerCase(), Encoder.encodePathPart(RFC3986_RESERVED));
  }

  @Test
  public void testUnicodeCharacters() {
    String s = "€";
    assertEquals("%E2%82%AC".toLowerCase(), Encoder.encodePathPart(s));
  }

  @Test
  public void uriDecoding() throws URISyntaxException {
    String decodedValue = RFC3986_UNRESERVED + RFC3986_RESERVED + "0..1..a..z..A..Z..@..→⦿◎❯❵₪€¥$£₧♤♡☁☂☔⚾☯®©℗😃";

    String encodedPath = Encoder.encodePathPart(decodedValue) + "/" + Encoder.encodePathPart(decodedValue);
    String encodedQuery = Encoder.encodePathPart(decodedValue);
    URI uri = new URI("http://host:80/" + encodedPath + "?" + encodedQuery + "=" + encodedQuery);

    assertEquals(uri.getPath(), "/" + decodedValue + "/" + decodedValue);
    assertEquals(uri.getQuery(), decodedValue + "=" + decodedValue);

    assertEquals(uri.getRawPath(), "/" + encodedPath);
    assertEquals(uri.getRawQuery(), encodedQuery + "=" + encodedQuery);
  }

}
