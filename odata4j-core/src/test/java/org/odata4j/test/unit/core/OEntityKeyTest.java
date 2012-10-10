package org.odata4j.test.unit.core;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.core.NamedValue;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OEntityKey.KeyType;
import org.odata4j.core.OProperties;

public class OEntityKeyTest {

  @Test
  public void parseTests() {
    Assert.assertEquals(k(1), OEntityKey.parse("(1)"));
    Assert.assertEquals(k(1), OEntityKey.parse("1"));
    Assert.assertEquals(k(1L), OEntityKey.parse("(1L)"));
    Assert.assertEquals(k("a"), OEntityKey.parse("('a')"));
    Assert.assertEquals(k("a"), OEntityKey.parse("(s='a')"));
    Assert.assertEquals(k("PartitionKey", "", "RowKey", "1"), OEntityKey.parse("(PartitionKey='',RowKey='1')"));
    Assert.assertEquals(k(new BigDecimal("43.9000")), OEntityKey.parse("(43.9000M)"));
  }

  @Test
  public void createTests() {
    sk("1", 1);
    sk("1L", 1L);
    sk("1", (short) 1);
    sk("-5", (byte) -5);
    sk("true", true);
    sk("'1'", "1");
    sk("'1'", '1');
    sk("'quo''te'", "quo'te");
    sk("1", OProperties.int32("n", 1), 1);

    Assert.assertEquals(k(1).hashCode(), k(1).hashCode());
    Assert.assertEquals(k(1), k(1));

    ck("a=1,b=2", OProperties.int32("a", 1), OProperties.int32("b", 2));
    ck("a=1,b=2", k("a", 1, "b", 2));

    i((Object[]) null);
    i();
    i(1, 1);
    i(OProperties.int32("a", 1), 1);
    i(OProperties.int32("a", 1), null);
    i(OProperties.int32("a", 1), OProperties.string("b", null));
    i(OProperties.int32("a", 1), OProperties.int32(null, 2));
    i(OProperties.int32("a", 1), OProperties.int32("", 2));
    i(new StringBuilder());

  }

  private static void i(Object... values) {
    try {
      OEntityKey.create(values);
    } catch (IllegalArgumentException e) {
      // expected
      return;
    }
    Assert.fail("Did not throw expected IllegalArgumentException");
  }

  private static OEntityKey k(Object value) {
    return OEntityKey.create(value);
  }

  private static OEntityKey k(Object... nameValues) {
    Map<String, Object> rt = new HashMap<String, Object>();
    for (int i = 0; i < nameValues.length; i += 2) {
      String name = (String) nameValues[i];
      Object value = nameValues[i + 1];
      rt.put(name, value);
    }
    return OEntityKey.create(rt);
  }

  private static void ck(String keyString, NamedValue<?>... nvs) {
    OEntityKey k = OEntityKey.create((Object[]) nvs);
    ck(keyString, k);
  }

  private static void ck(String keyString, OEntityKey k) {
    Assert.assertNotNull(k);
    Assert.assertTrue(k.getKeyType() == KeyType.COMPLEX);
    Assert.assertEquals("(" + keyString + ")", k.toKeyString());
  }

  private static void sk(String keyString, Object singleValue) {
    sk(keyString, singleValue, singleValue);
  }

  private static void sk(String keyString, Object singleValueInput, Object singleValue) {
    OEntityKey k = k(singleValueInput);
    Assert.assertNotNull(k);
    Assert.assertTrue(k.getKeyType() == KeyType.SINGLE);
    Assert.assertEquals(singleValue, k.asSingleValue());
    Assert.assertEquals("(" + keyString + ")", k.toKeyString());
  }
}
