package com.sap.core.odata.fit;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.rest.ODataApplication;




public class FitApplication extends ODataApplication {
  @Override
  protected Class<? extends ContextResolver<ODataSingleProcessor>> getContextResolver() {
    return FitContextResolver.class;
  }

  public static void setProducerInstance(ODataSingleProcessor producer) {
    FitContextResolver.setProducerInstance(producer);
  }
}