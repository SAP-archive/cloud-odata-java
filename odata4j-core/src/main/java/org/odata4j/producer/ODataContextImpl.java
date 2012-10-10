package org.odata4j.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.HttpHeaders;

/**
 * An implementation of ODataContext
 */
public class ODataContextImpl implements ODataContext {

  private ODataContextImpl() {}

  @Override
  public <T> T getContextAspect(Class<T> contextClass) {
    for (Entry<Class, Object> aspect : contexts.entrySet()) {
      if (contextClass.isAssignableFrom(aspect.getKey())) {
        return (T) aspect.getValue();
      }
    }
    return null;
  }

  @Override
  public ODataHeadersContext getRequestHeadersContext() {
    return getContextAspect(ODataHeadersContext.class);
  }

  public static ODataContextBuilder builder() {
    return new ODataContextBuilder();
  }

  public static class ODataContextBuilder {
    protected ODataContextBuilder() {}

    public ODataContextBuilder aspect(Object aspect) {

      // in Jersy, ContainerRequest implements a bunch of interfaces (
      // HttpRequestContext, Traceable, HttpHeaders, Reqeust, SecurityContext).

      // JohnS wants to avoid coupling with javax.ws.rs things whenever
      // possible.  So, we wrap things we provide wrappers for (and also expose
      // them as their native things).  I'm not really sure of the value
      // of this...isn't odata4j intrinsically linked to javax.ws.rs already?
      // it's not like we are going to swap that out...

      if (HttpHeaders.class.isAssignableFrom(aspect.getClass())) {
        impl.addContextAspect(new ODataHeadersImpl((HttpHeaders) aspect));
      }

      impl.addContextAspect(aspect);
      return this;
    }

    public ODataContextImpl build() {
      return impl;
    }

    private ODataContextImpl impl = new ODataContextImpl();
  }

  private void addContextAspect(Object aspect) {
    contexts.put(aspect.getClass(), aspect);
  }

  private Map<Class, Object> contexts = new HashMap<Class, Object>();
}
