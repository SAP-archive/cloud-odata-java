package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.commons.ODataHttpMethod;

/**
 * @author SAP AG
 */
public class ODataContextImplTest {

  @Test
  public void httpMethod() {
    ODataContextImpl context = new ODataContextImpl();
    context.setHttpMethod(ODataHttpMethod.GET.name());
    assertEquals(ODataHttpMethod.GET.name(), context.getHttpMethod());
  }

  @Test
  public void debugMode() {
    ODataContextImpl context = new ODataContextImpl();
    context.setDebugMode(true);
    assertTrue(context.isInDebugMode());
  }
}
