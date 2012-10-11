package com.sap.core.odata.fit;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.core.odata.producer.ODataProducer;

@Provider
public class FitContextResolver implements ContextResolver<ODataProducer> {

  protected static final Logger log = LoggerFactory.getLogger(FitContextResolver.class);

  private static ODataProducer fitProducer;

  @Override
  public ODataProducer getContext(Class<?> type) {

    if (FitContextResolver.fitProducer == null) {
      /* faile earlier */
      throw new NullPointerException();
    }

    FitContextResolver.log.debug("mockProducer class: " + FitContextResolver.fitProducer.getClass().getCanonicalName());

    return FitContextResolver.fitProducer;
  }

  public static void setProducerInstance(ODataProducer producer) {
    FitContextResolver.fitProducer = producer;
  }
}
