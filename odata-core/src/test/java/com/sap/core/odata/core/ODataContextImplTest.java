package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataRequest;

/**
 * @author SAP AG
 */
public class ODataContextImplTest {

  ODataContextImpl context;

  @Before
  public void before() {
    ODataServiceFactory factory = mock(ODataServiceFactory.class);
    ODataRequest request = mock(ODataRequest.class);

    when(request.getMethod()).thenReturn(ODataHttpMethod.GET);
    when(request.getPathInfo()).thenReturn(new PathInfoImpl());

    context = new ODataContextImpl(request, factory);
  }

  @Test
  public void httpMethod() {
    context.setHttpMethod(ODataHttpMethod.GET.name());
    assertEquals(ODataHttpMethod.GET.name(), context.getHttpMethod());
  }

  @Test
  public void debugMode() {
    context.setDebugMode(true);
    assertTrue(context.isInDebugMode());
  }
}
