package com.sap.core.odata.core.commons;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class DecoderTest extends BaseTest {

  @Test
  public void asciiCharacters() {
    assertEquals(null, Decoder.decode(null));

    String s = "azAZ019";
    assertEquals(s, Decoder.decode(s));

    s = "\"\\`{}|";
    assertEquals(s, Decoder.decode(s));
  }

  @Test
  public void asciiControl() {
    assertEquals("\b\t\n\r", Decoder.decode("%08%09%0a%0d"));
  }

  @Test
  public void asciiEncoded() {
    assertEquals("<>%&", Decoder.decode("%3c%3e%25%26"));
    assertEquals(":/?#[]@", Decoder.decode("%3a%2f%3f%23%5b%5d%40"));
    assertEquals(" !\"$'()*+,-.", Decoder.decode("%20%21%22%24%27%28%29%2A%2B%2C%2D%2E"));
  }

  @Test
  public void unicodeCharacters() {
    assertEquals("€", Decoder.decode("%e2%82%ac"));
    assertEquals("\uFDFC", Decoder.decode("%ef%b7%bc")); // RIAL SIGN
  }

  @Test
  public void charactersOutsideBmp() {
    // Unicode characters outside the Basic Multilingual Plane are stored
    // in a Java String in two surrogate characters.
    assertEquals(String.valueOf(Character.toChars(0x1F603)), Decoder.decode("%f0%9f%98%83"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongCharacter() {
    Decoder.decode("%20ä");
  }

  @Test(expected = NumberFormatException.class)
  public void wrongPercentNumber() {
    Decoder.decode("%-3");
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongPercentPercent() {
    Decoder.decode("%%a");
  }

  @Test(expected = IllegalArgumentException.class)
  public void unfinishedPercent() {
    Decoder.decode("%a");
  }
}
