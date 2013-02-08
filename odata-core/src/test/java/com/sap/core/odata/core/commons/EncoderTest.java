package com.sap.core.odata.core.commons;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class EncoderTest extends BaseTest {

  private final static String RFC3986_UNRESERVED = "-._~"; // + ALPHA + DIGIT
  private final static String RFC3986_GEN_DELIMS = ":/?#[]@";
  private final static String RFC3986_SUB_DELIMS = "!$&'()*+,;=";
  private final static String RFC3986_RESERVED = RFC3986_GEN_DELIMS + RFC3986_SUB_DELIMS;

  @Test
  public void asciiCharacters() {
    String s = "azAZ019";
    assertEquals(s, Encoder.encode(s));
    assertEquals(s, Encoder.encode(s));

    s = "\"\\`{}|";
    assertEquals(s, Encoder.encode(s));
  }

  @Test
  public void asciiControl() {
    assertEquals("%08%09%0a%0d", Encoder.encode("\b\t\n\r"));
  }

  @Test
  public void unsafe() {
    assertEquals("%3c%3e%25%26", Encoder.encode("<>%&"));
  }

  @Test
  public void rfc3986Unreserved() {
    assertEquals(RFC3986_UNRESERVED, Encoder.encode(RFC3986_UNRESERVED));
  }

  @Test
  public void rfc3986GenDelims() {
    assertEquals("%3a%2f%3f%23%5b%5d%40", Encoder.encode(RFC3986_GEN_DELIMS));
  }

  @Test
  public void rfc3986SubDelims() {
    assertEquals("!$%26'()*+,;=", Encoder.encode(RFC3986_SUB_DELIMS));
  }

  @Test
  public void rfc3986Reserved() {
    assertEquals("%3a%2f%3f%23%5b%5d%40!$%26'()*+,;=", Encoder.encode(RFC3986_RESERVED));
  }

  @Test
  public void unicodeCharacters() {
    assertEquals("%e2%82%ac", Encoder.encode("€"));
    assertEquals("%ef%b7%bc", Encoder.encode("\uFDFC")); // RIAL SIGN
  }

  @Test
  public void charactersOutsideBmp() {
    // Unicode characters outside the Basic Multilingual Plane are stored
    // in a Java String in two surrogate characters.
    final String s = String.valueOf(Character.toChars(0x1F603));
    assertEquals("%f0%9f%98%83", Encoder.encode(s));
  }

  @Test
  public void uriDecoding() throws URISyntaxException {
    String decodedValue = RFC3986_UNRESERVED + RFC3986_RESERVED + "0..1..a..z..A..Z..@"
        + "\u2323\uFDFC" + String.valueOf(Character.toChars(0x1F603));

    String encodedPath = Encoder.encode(decodedValue) + "/" + Encoder.encode(decodedValue);
    String encodedQuery = Encoder.encode(decodedValue);
    URI uri = new URI("http://host:80/" + encodedPath + "?" + encodedQuery + "=" + encodedQuery);

    assertEquals(uri.getPath(), "/" + decodedValue + "/" + decodedValue);
    assertEquals(uri.getQuery(), decodedValue + "=" + decodedValue);

    assertEquals(uri.getRawPath(), "/" + encodedPath);
    assertEquals(uri.getRawQuery(), encodedQuery + "=" + encodedQuery);
  }

}
