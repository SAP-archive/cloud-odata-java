package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.ref.processor.ScenarioProcessor;

@Provider
public class ScenarioResolver implements ContextResolver<ODataProcessor> {

  @Override
  public ODataProcessor getContext(Class<?> type) {
    return new ScenarioProcessor();
  }
}
