/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class DecoderTest extends BaseTest {

  @Test
  public void asciiCharacters() {
    assertNull(Decoder.decode(null));

    String s = "azAZ019";
    assertEquals(s, Decoder.decode(s));

    s = "\"\\`{}|";
    assertEquals(s, Decoder.decode(s));
  }

  @Test
  public void asciiControl() {
    assertEquals("\u0000\b\t\n\r", Decoder.decode("%00%08%09%0a%0d"));
  }

  @Test
  public void asciiEncoded() {
    assertEquals("<>%&", Decoder.decode("%3c%3e%25%26"));
    assertEquals(":/?#[]@", Decoder.decode("%3a%2f%3f%23%5b%5d%40"));
    assertEquals(" !\"$'()*+,-.", Decoder.decode("%20%21%22%24%27%28%29%2A%2B%2C%2D%2E"));
  }

  @Test
  public void unicodeCharacters() {
    assertEquals("€", Decoder.decode("%E2%82%AC"));
    assertEquals("\uFDFC", Decoder.decode("%EF%B7%BC"));
  }

  @Test
  public void charactersOutsideBmp() {
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

  @Test(expected = IllegalArgumentException.class)
  public void nullByte() {
    Decoder.decode("%\u0000ff");
  }
}
