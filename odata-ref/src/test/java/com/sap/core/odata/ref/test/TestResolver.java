package com.sap.core.odata.ref.test;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.core.producer.ODataProducer;

@Provider
public class TestResolver implements ContextResolver<ODataProducer> {

  private static ODataProducer producer;

  @Override
  public ODataProducer getContext(Class<?> type) {

    assertNotNull(TestResolver.producer);
    
    return TestResolver.producer;
  }

  public static void setProducerInstance(ODataProducer producer) {
    TestResolver.producer = producer;
  }
}
