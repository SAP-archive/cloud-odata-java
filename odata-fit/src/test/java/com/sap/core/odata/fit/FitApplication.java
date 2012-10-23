package com.sap.core.odata.fit;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.rest.ODataApplication;


public class FitApplication extends ODataApplication {
  @Override
  protected Class<? extends ContextResolver<ODataProcessor>> getContextResolver() {
    return FitContextResolver.class;
  }

  public static void setProcessorInstance(ODataProcessor processor) {
    FitContextResolver.setProcessorInstance(processor);
  }
}