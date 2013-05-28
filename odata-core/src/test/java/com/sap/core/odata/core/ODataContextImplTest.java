package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ODataContextImplTest {

  private static final String HTTP_GET = "GET";

  @Test
  public void httpMethodTest() {
    ODataContextImpl context = new ODataContextImpl();
    context.setHttpMethod(HTTP_GET);

    assertEquals(HTTP_GET, context.getHttpMethod());
  }
}
