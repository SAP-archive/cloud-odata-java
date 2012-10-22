package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.ref.producer.ScenarioProducer;

@Provider
public class ScenarioResolver implements ContextResolver<ODataSingleProcessor> {

  @Override
  public ODataSingleProcessor getContext(Class<?> type) {
    return new ScenarioProducer();
  }
}
