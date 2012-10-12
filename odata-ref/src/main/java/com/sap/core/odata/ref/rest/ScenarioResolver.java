package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.ref.producer.ScenarioProducer;

@Provider
public class ScenarioResolver implements ContextResolver<ODataProducer> {

  @Override
  public ODataProducer getContext(Class<?> type) {
    return new ScenarioProducer();
  }
}
