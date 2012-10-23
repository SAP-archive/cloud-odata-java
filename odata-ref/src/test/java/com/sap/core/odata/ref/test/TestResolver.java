package com.sap.core.odata.ref.test;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.api.processor.ODataProcessor;

@Provider
public class TestResolver implements ContextResolver<ODataProcessor> {

  private static ODataProcessor processor;

  @Override
  public ODataProcessor getContext(Class<?> type) {

    assertNotNull(TestResolver.processor);
    
    return TestResolver.processor;
  }

  public static void setProducerInstance(ODataProcessor processor) {
    TestResolver.processor = processor;
  }
}
