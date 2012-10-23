package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.rest.ODataApplication;


public class ScenarioApplication extends ODataApplication {

  @Override
  protected Class<? extends ContextResolver<ODataProcessor>> getContextResolver() {
    return ScenarioResolver.class;
  }
}
