package com.sap.core.odata.ref.test;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.api.processor.ODataSingleProcessor;

@Provider
public class TestResolver implements ContextResolver<ODataSingleProcessor> {

  private static ODataSingleProcessor producer;

  @Override
  public ODataSingleProcessor getContext(Class<?> type) {

    assertNotNull(TestResolver.producer);
    
    return TestResolver.producer;
  }

  public static void setProducerInstance(ODataSingleProcessor producer) {
    TestResolver.producer = producer;
  }
}
