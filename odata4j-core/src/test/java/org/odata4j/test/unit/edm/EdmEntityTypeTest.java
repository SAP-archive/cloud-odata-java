package org.odata4j.test.unit.edm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.odata4j.edm.EdmEntityType;

public class EdmEntityTypeTest {
  private static final Boolean OPENTYPE_TRUE = true;

  @Test
  public void testOpenType() {
    EdmEntityType.Builder builder = EdmEntityType.newBuilder();
    EdmEntityType entityType = builder.setName("Product").setOpenType(OPENTYPE_TRUE).addKeys("ProductId").build();
    assertEquals(OPENTYPE_TRUE, entityType.getOpenType());
  }

}
