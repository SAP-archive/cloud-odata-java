package org.odata4j.producer.resources;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * Root application containing resources for cross domain and client access policies.
 */
public class RootApplication extends Application {

  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> classes = new HashSet<Class<?>>();
    classes.add(CrossDomainXmlResource.class);
    classes.add(ClientAccessPolicyXmlResource.class);
    return classes;
  }
}
