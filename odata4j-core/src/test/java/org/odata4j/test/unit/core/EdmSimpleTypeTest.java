package org.odata4j.test.unit.core;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.edm.EdmSimpleType;

public class EdmSimpleTypeTest {

  @Test
  public void edmSimpleTypeTests() {
    Assert.assertTrue(EdmSimpleType.STRING.getJavaTypes().contains(String.class));
  }
}
