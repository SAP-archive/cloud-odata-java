package org.odata4j.producer.resources;

import java.util.Set;

/**
 * Default OData application containing the {@link DefaultODataProducerProvider}.
 */
public final class DefaultODataApplication extends AbstractODataApplication {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = super.getClasses();
    classes.add(DefaultODataProducerProvider.class);
    return classes;
  }
}
