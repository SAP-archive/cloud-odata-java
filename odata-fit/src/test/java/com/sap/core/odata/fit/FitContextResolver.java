package com.sap.core.odata.fit;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.processor.ODataProcessor;

@Provider
public class FitContextResolver implements ContextResolver<ODataProcessor> {

  protected static final Logger log = LoggerFactory.getLogger(FitContextResolver.class);

  private static ODataProcessor fitProcessor;

  @Override
  public ODataProcessor getContext(Class<?> type) {

    if (FitContextResolver.fitProcessor == null) {
      /* faile earlier */
      throw new NullPointerException();
    }

    FitContextResolver.log.debug("mockProducer class: " + FitContextResolver.fitProcessor.getClass().getCanonicalName());

    return FitContextResolver.fitProcessor;
  }

  public static void setProcessorInstance(ODataProcessor producer) {
    FitContextResolver.fitProcessor = producer;
  }
}
