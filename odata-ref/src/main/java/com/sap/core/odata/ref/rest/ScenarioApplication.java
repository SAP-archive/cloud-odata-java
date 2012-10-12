package com.sap.core.odata.ref.rest;

import javax.ws.rs.ext.ContextResolver;

import com.sap.core.odata.core.producer.ODataProducer;
import com.sap.core.odata.core.rest.ODataApplication;


public class ScenarioApplication extends ODataApplication {

  @Override
  protected Class<? extends ContextResolver<ODataProducer>> getContextResolver() {
    // TODO Auto-generated method stub
    return null;
  }

}
