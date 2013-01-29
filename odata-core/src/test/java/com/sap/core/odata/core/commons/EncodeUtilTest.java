package com.sap.core.odata.core.commons;

import org.junit.Test;
import static org.junit.Assert.*;

import com.sap.core.odata.testutil.fit.BaseTest;

public class EncodeUtilTest extends BaseTest {

  
  @Test
  public void testAsciiCharacters() {
    String s = "azAZ0123456789";
    assertEquals(s, EncoderUtil.encode(s));
  }
  
  @Test
  public void testUnsafeCharacters() {
    String s = " #$%&+,/:;=?@[]<>";
    assertEquals("%20%23%24%25%26%2B%2C%2F%3A%3B%3D%3F%40%5B%5D%3C%3E".toLowerCase(), EncoderUtil.encode(s));
  }
  
  @Test
  public void testSafeCharacters() {
    String s = "_-!.~'()*\"";
    assertEquals(s, EncoderUtil.encode(s));
  }

  @Test
  public void testUnicodeCharacters() {
    String s = "€";
    assertEquals("%E2%82%AC".toLowerCase(), EncoderUtil.encode(s));
  }
  
}
