package com.sap.core.odata.fit;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.producer.ODataProducer;
import com.sap.core.odata.rest.ODataApplication;




public class FitApplication extends ODataApplication {
  @Override
  protected Class<? extends ContextResolver<ODataProducer>> getContextResolver() {
    return FitContextResolver.class;
  }
}