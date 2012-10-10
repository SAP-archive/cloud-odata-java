package org.odata4j.test.unit.core;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.core.UnsignedByte;
import org.odata4j.test.unit.Asserts;

public class UnsignedByteTest {

  @Test
  public void unsignedByteTests() {
    Assert.assertEquals(0, UnsignedByte.MIN_VALUE.intValue());
    Assert.assertEquals(255, UnsignedByte.MAX_VALUE.intValue());
    Assert.assertEquals("234", new UnsignedByte(234).toString());
    Assert.assertEquals((byte) 123, new UnsignedByte(123).byteValue());
    Assert.assertEquals(UnsignedByte.MIN_VALUE, UnsignedByte.valueOf(0));
    Assert.assertEquals(UnsignedByte.MIN_VALUE, UnsignedByte.parseUnsignedByte("000"));

    UnsignedByte one = UnsignedByte.valueOf(1);
    UnsignedByte two = UnsignedByte.valueOf(2);

    Assert.assertTrue(one.compareTo(two) < 0);
    Assert.assertTrue(one.compareTo(one) == 0);
    Assert.assertTrue(two.compareTo(one) > 0);
    Assert.assertTrue(one.equals(new UnsignedByte(1)));
    Assert.assertTrue(!one.equals(null));

    Asserts.assertThrows(IllegalArgumentException.class, newUnsignedByte(-1));
    Asserts.assertThrows(IllegalArgumentException.class, newUnsignedByte(256));
    Asserts.assertThrows(IllegalArgumentException.class, valueOf(-1));
    Asserts.assertThrows(IllegalArgumentException.class, valueOf(256));
    Asserts.assertThrows(IllegalArgumentException.class, parseUnsignedByte("-1"));
    Asserts.assertThrows(IllegalArgumentException.class, parseUnsignedByte("256"));
    Asserts.assertThrows(NumberFormatException.class, parseUnsignedByte(""));
    Asserts.assertThrows(NumberFormatException.class, parseUnsignedByte("a"));
  }

  private static Runnable newUnsignedByte(final int value) {
    return new Runnable() {
      @Override
      public void run() {
        new UnsignedByte(value);
      }
    };
  }

  private static Runnable valueOf(final int value) {
    return new Runnable() {
      @Override
      public void run() {
        UnsignedByte.valueOf(value);
      }
    };
  }

  private static Runnable parseUnsignedByte(final String value) {
    return new Runnable() {
      @Override
      public void run() {
        UnsignedByte.parseUnsignedByte(value);
      }
    };
  }

}
