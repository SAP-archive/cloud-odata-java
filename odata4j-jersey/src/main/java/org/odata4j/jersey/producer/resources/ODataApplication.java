package org.odata4j.jersey.producer.resources;

import java.util.Set;

import org.odata4j.producer.resources.AbstractODataApplication;

/**
 * Jersey-specific OData application containing the Jersey-specific
 * {@link ODataProducerProvider}.
 */
public class ODataApplication extends AbstractODataApplication {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = super.getClasses();
    classes.add(ODataProducerProvider.class);
    return classes;
  }
}
