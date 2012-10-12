package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.core.producer.ODataProducer;

public class ScenarioResolver implements ContextResolver<ODataProducer> {

  @Override
  public ODataProducer getContext(Class<?> type) {
    return new ODataProducer() {};
  }
}
