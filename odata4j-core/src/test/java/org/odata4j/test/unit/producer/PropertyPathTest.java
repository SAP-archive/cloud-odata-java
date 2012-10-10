package org.odata4j.test.unit.producer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.odata4j.producer.PropertyPath;

/**
 * Tests for {@link PropertyPath}
 */
public class PropertyPathTest {

  @Test
  public void testEmptyPath() {
    PropertyPath p = new PropertyPath("");
    assertTrue(p.isEmpty());
    assertTrue(p.getNComponents() == 0);
    assertTrue(p.getLastComponent() == null);
    assertTrue(p.getPath().equals(""));
    assertFalse(p.isWild());
    assertTrue(p.equals(new PropertyPath("")));
    p = p.addComponent("blar");
    assertTrue(p.equals(new PropertyPath("blar")));

    assertTrue(p.startsWith(new PropertyPath("")));
    assertFalse(p.startsWith(new PropertyPath("foo/bar")));
  }

  @Test
  public void testOnePath() {
    PropertyPath p = new PropertyPath("foo");
    assertFalse(p.isEmpty());
    assertTrue(p.getNComponents() == 1);
    assertTrue(p.getLastComponent().equals("foo"));
    assertTrue(p.getPath().equals("foo"));
    assertFalse(p.isWild());
    assertTrue(p.equals(new PropertyPath("foo")));
    p = p.addComponent("blar");
    assertTrue(p.equals(new PropertyPath("foo/blar")));

    assertTrue(p.startsWith(new PropertyPath("")));
    assertTrue(p.startsWith(new PropertyPath("foo")));
    assertTrue(p.startsWith(new PropertyPath("foo/blar")));
    assertFalse(p.startsWith(new PropertyPath("foobar/blar")));
  }

  @Test
  public void testMultiPath() {
    PropertyPath p = new PropertyPath("foo/bar");
    assertFalse(p.isEmpty());
    assertTrue(p.getNComponents() == 2);
    assertTrue(p.getLastComponent().equals("bar"));
    assertTrue(p.getPath().equals("foo/bar"));
    assertFalse(p.isWild());
    assertTrue(p.equals(new PropertyPath("foo/bar")));
    p = p.addComponent("blar");
    assertTrue(p.equals(new PropertyPath("foo/bar/blar")));
  }

  @Test
  public void testOneWild() {
    PropertyPath p = new PropertyPath("*");
    assertFalse(p.isEmpty());
    assertTrue(p.getNComponents() == 1);
    assertTrue(p.getLastComponent().equals("*"));
    assertTrue(p.getPath().equals("*"));
    assertTrue(p.isWild());
    assertTrue(p.equals(new PropertyPath("*")));
  }

  @Test
  public void testMultiWild() {
    PropertyPath p = new PropertyPath("foo/bar/*");
    assertFalse(p.isEmpty());
    assertTrue(p.getNComponents() == 3);
    assertTrue(p.getLastComponent().equals("*"));
    assertTrue(p.getPath().equals("foo/bar/*"));
    assertTrue(p.isWild());
    assertTrue(p.equals(new PropertyPath("foo/bar/*")));
  }

  @Test
  public void testRemoveFirst() {
    PropertyPath p = new PropertyPath("");
    p = p.removeFirstComponent();
    assertTrue(p.isEmpty());

    p = new PropertyPath("foobar");
    p = p.removeFirstComponent();
    assertTrue(p.isEmpty());

    p = new PropertyPath("foo/bar/blat");
    p = p.removeFirstComponent();
    assertFalse(p.isEmpty());
    assertTrue(p.equals(new PropertyPath("bar/blat")));
  }

  @Test
  public void testRemoveLast() {
    PropertyPath p = new PropertyPath("");
    p = p.removeLastComponent();
    assertTrue(p.isEmpty());

    p = new PropertyPath("foobar");
    p = p.removeLastComponent();
    assertTrue(p.isEmpty());

    p = new PropertyPath("foo/bar/blat");
    p = p.removeLastComponent();
    assertFalse(p.isEmpty());
    assertTrue(p.equals(new PropertyPath("foo/bar")));
  }
}
