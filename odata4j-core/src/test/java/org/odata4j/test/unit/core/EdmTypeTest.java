package org.odata4j.test.unit.core;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.edm.EdmType;

public class EdmTypeTest {

  @Test
  public void edmTypeTests() {
    Assert.assertTrue(EdmType.getSimple("Edm.String").isSimple()); // keep this test first, or at least before EdmSimpleType is loaded
    Assert.assertTrue(EdmType.getSimple("My.Custom.Type") == null);
  }
}
