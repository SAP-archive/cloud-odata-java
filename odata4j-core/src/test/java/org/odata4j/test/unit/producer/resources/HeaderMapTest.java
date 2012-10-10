package org.odata4j.test.unit.producer.resources;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.odata4j.producer.resources.HeaderMap;

public class HeaderMapTest {

  private static final String VALUE_1 = "value1";
  private static final String VALUE_2 = "value2";
  private static final String VALUE_3 = "value3";
  private static final String VALUE_4 = "value4";

  private static List<String> list1 = new ArrayList<String>();
  private static List<String> list2 = new ArrayList<String>();

  private HeaderMap headers = null;

  @BeforeClass
  public static void startupClass() {
    list1.add(VALUE_1);
    list1.add(VALUE_2);
    list2.add(VALUE_3);
    list2.add(VALUE_4);
  }

  @Before
  public void startup() {
    headers = new HeaderMap();
  }

  @Test
  public void testPutAndGet() {
    Assert.assertEquals(0, headers.size());
    Assert.assertNull(headers.get("key"));
    Assert.assertNull(headers.put("key", list1));
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(VALUE_1, headers.get("key").get(0));
    Assert.assertEquals(VALUE_2, headers.get("KEY").get(1));

    Assert.assertEquals(VALUE_1, headers.put("key", list2).get(0));
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(VALUE_3, headers.get("key").get(0));
    Assert.assertEquals(VALUE_4, headers.get("KEY").get(1));

    Assert.assertEquals(VALUE_3, headers.put("KEY", list1).get(0));
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(VALUE_1, headers.get("key").get(0));
    Assert.assertEquals(VALUE_2, headers.get("KEY").get(1));
  }

  @Test
  public void testContainsKey() {
    Assert.assertEquals(0, headers.size());
    Assert.assertFalse(headers.containsKey("key"));
    Assert.assertFalse(headers.containsKey("KEY"));
    Assert.assertNull(headers.put("key", list1));
    Assert.assertEquals(1, headers.size());
    Assert.assertTrue(headers.containsKey("key"));
    Assert.assertTrue(headers.containsKey("KEY"));
  }

  @Test
  public void testRemove() {
    Assert.assertEquals(0, headers.size());
    Assert.assertNull(headers.remove("KEY"));
    Assert.assertNull(headers.put("key", list1));
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(VALUE_1, headers.remove("KEY").get(0));
    Assert.assertEquals(0, headers.size());
  }

  @Test
  public void testPutSingleAndAdd() {
    Assert.assertEquals(0, headers.size());
    headers.putSingle("key", VALUE_1);
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(1, headers.get("key").size());
    headers.add("KEY", VALUE_2);
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(2, headers.get("KEY").size());
  }

  @Test
  public void testGetFirst() {
    Assert.assertEquals(0, headers.size());
    Assert.assertNull(headers.getFirst("KEY"));
    Assert.assertNull(headers.put("key", list1));
    Assert.assertEquals(1, headers.size());
    Assert.assertEquals(VALUE_1, headers.getFirst("KEY"));
  }
}
