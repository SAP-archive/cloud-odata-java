package org.odata4j.test.unit.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.odata4j.core.ODataHttpMethod;

public class ODataHttpMethodTest {

  @Test
  public void testFromString() {
    assertEquals(ODataHttpMethod.GET, ODataHttpMethod.fromString("get"));
    assertEquals(ODataHttpMethod.PUT, ODataHttpMethod.fromString("put"));
    assertEquals(ODataHttpMethod.POST, ODataHttpMethod.fromString("post"));
    assertEquals(ODataHttpMethod.DELETE, ODataHttpMethod.fromString("delete"));
    assertEquals(ODataHttpMethod.MERGE, ODataHttpMethod.fromString("merge"));
    assertEquals(ODataHttpMethod.PATCH, ODataHttpMethod.fromString("patch"));
    assertEquals(ODataHttpMethod.OPTIONS, ODataHttpMethod.fromString("options"));
    assertEquals(ODataHttpMethod.HEAD, ODataHttpMethod.fromString("head"));
  }
}
