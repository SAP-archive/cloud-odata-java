package com.sap.core.odata.ref.test;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.core.rest.ODataApplication;
import com.sap.core.odata.core.rest.ODataRootLocator;


public class TestApplication extends ODataApplication {

  @Override
  protected Class<? extends ContextResolver<ODataProducer>> getContextResolver() {
    return TestResolver.class;
  }

  @Override
  protected Class<? extends ODataRootLocator> getRootResourceLocator() {
    return TestRootLocator.class;
  }

  public static void setProducerInstance(ODataProducer producer) {
    TestResolver.setProducerInstance(producer);
  }

}
