package com.sap.core.odata.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;

import org.odata4j.producer.resources.ExceptionMappingProvider;

import com.sap.core.odata.producer.ODataProducer;

public abstract class ODataApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(this.getSubResourceLocator());
    classes.add(this.getContextResolver());
    classes.add(ExceptionMappingProvider.class);
    return classes;
  }

  @Override
  public Set<Object> getSingletons() {
    return Collections.emptySet();
  }
  
  protected Class<?> getSubResourceLocator() {
    return ODataSubResourceLocator.class;
  }

  protected abstract Class<? extends ContextResolver<ODataProducer>> getContextResolver();

}
