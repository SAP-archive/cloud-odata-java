package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.core.rest.ODataApplication;
import com.sap.core.odata.core.rest.ODataRootLocator;


public class ScenarioApplication extends ODataApplication {

  @Override
  protected Class<? extends ContextResolver<ODataSingleProcessor>> getContextResolver() {
    return ScenarioResolver.class;
  }

  @Override
  protected Class<? extends ODataRootLocator> getRootResourceLocator() {
    return ScenarioRootLocator.class;
  }

}
