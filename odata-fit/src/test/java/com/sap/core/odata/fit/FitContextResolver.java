package com.sap.core.odata.fit;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.api.processor.ODataSingleProcessor;

@Provider
public class FitContextResolver implements ContextResolver<ODataSingleProcessor> {

  protected static final Logger log = LoggerFactory.getLogger(FitContextResolver.class);

  private static ODataSingleProcessor fitProducer;

  @Override
  public ODataSingleProcessor getContext(Class<?> type) {

    if (FitContextResolver.fitProducer == null) {
      /* faile earlier */
      throw new NullPointerException();
    }

    FitContextResolver.log.debug("mockProducer class: " + FitContextResolver.fitProducer.getClass().getCanonicalName());

    return FitContextResolver.fitProducer;
  }

  public static void setProducerInstance(ODataSingleProcessor producer) {
    FitContextResolver.fitProducer = producer;
  }
}
