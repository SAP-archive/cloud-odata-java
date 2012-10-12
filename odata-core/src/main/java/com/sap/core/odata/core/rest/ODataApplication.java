package com.sap.core.odata.core.rest;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.odata4j.producer.resources.ExceptionMappingProvider;

import com.sap.core.odata.core.producer.ODataProducer;

/**
 * Abstract OData JAX-RS application defines the OData REST framework as entry point for generic REST servlet e.g. CXFNoSpringJaxrsServlet or others.
 * @see Application
 */
public abstract class ODataApplication extends Application {

  /**
   * OData default root resources and providers. 
   * @see Applicaion
   */
  @Override
  public Set<Class<?>> getClasses() {
    Class<? extends ContextResolver<ODataProducer>> resolver = this.getContextResolver();
    Class<?> locator = this.getRootResourceLocator();
 
    if (resolver.getAnnotation(Provider.class) == null) {
      throw new RuntimeException("missing @Provider annotation: "+ resolver.getCanonicalName());
    }
    
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(locator);
    classes.add(resolver);
    classes.add(ExceptionMappingProvider.class);
    return classes;
  }

  /**
   * Singletons are not recommended because they break the state less REST principle.
   */
  @Override
  public Set<Object> getSingletons() {
    return Collections.emptySet();
  }
  
  /**
   * 
   * @return
   */
  protected Class<? extends ODataRootLocator> getRootResourceLocator() {
    return ODataRootLocator.class;
  }

  protected abstract Class<? extends ContextResolver<ODataProducer>> getContextResolver();

}
